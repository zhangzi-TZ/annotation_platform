package com.annotationplatform.service;

import com.annotationplatform.config.VideoPreviewProperties;
import com.annotationplatform.model.VideoSlot;
import com.annotationplatform.storage.VideoStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class VideoPreviewService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VideoPreviewService.class);

    private final VideoPreviewProperties properties;

    public VideoPreviewService(VideoPreviewProperties properties) {
        this.properties = properties;
    }

    public Optional<PreviewResult> generatePreview(String sessionId,
                                                   VideoSlot slot,
                                                   VideoStorageService.StoredVideo storedVideo) {
        if (!properties.isEnabled()) {
            return Optional.empty();
        }
        Path source = storedVideo.getPath();
        if (source == null || !Files.exists(source)) {
            LOGGER.warn("Cannot generate preview; source video missing for session {} slot {}", sessionId, slot);
            return Optional.empty();
        }
        String previewFilename = buildPreviewFilename(storedVideo.getFilename());
        Path previewPath = source.getParent().resolve(previewFilename);
        List<String> command = List.of(
                properties.getFfmpegPath(),
                "-y",
                "-i",
                source.toAbsolutePath().toString(),
                "-c:v",
                "libx264",
                "-pix_fmt",
                "yuv420p",
                "-movflags",
                "+faststart",
                "-an",
                previewPath.toAbsolutePath().toString()
        );
        LOGGER.info("Generating preview for session {} slot {} using {}", sessionId, slot, previewPath.getFileName());
        try {
            ProcessBuilder builder = new ProcessBuilder(command);
            builder.redirectErrorStream(true);
            Process process = builder.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    LOGGER.debug("[ffmpeg] {}", line);
                }
            }
            int exit = process.waitFor();
            if (exit == 0) {
                return Optional.of(new PreviewResult(previewFilename));
            }
            LOGGER.warn("ffmpeg exited with code {} while creating preview for session {}", exit, sessionId);
            Files.deleteIfExists(previewPath);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.warn("Preview generation interrupted for session {} slot {}", sessionId, slot, e);
        } catch (IOException e) {
            LOGGER.warn("Failed to generate preview for session {} slot {}", sessionId, slot, e);
        }
        return Optional.empty();
    }

    private String buildPreviewFilename(String originalFilename) {
        String base = originalFilename != null ? originalFilename.replaceAll("\\.mp4$", "") : "preview";
        return base + "-preview-" + Instant.now().toEpochMilli() + "-" + UUID.randomUUID() + ".mp4";
    }

    public static class PreviewResult {
        private final String filename;

        public PreviewResult(String filename) {
            this.filename = filename;
        }

        public String getFilename() {
            return filename;
        }
    }
}

