package com.annotationplatform.storage;

import com.annotationplatform.config.StorageProperties;
import com.annotationplatform.model.VideoSlot;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.UUID;

@Component
public class VideoStorageService {

    private final StorageProperties storageProperties;

    public VideoStorageService(StorageProperties storageProperties) {
        this.storageProperties = storageProperties;
    }

    public StoredVideo save(String sessionId, VideoSlot slot, MultipartFile file) {
        try {
            Path sessionDir = storageProperties.videosPath().resolve(sessionId).resolve(slot.name().toLowerCase());
            Files.createDirectories(sessionDir);
            String extension = extractExtension(file.getOriginalFilename());
            String filename = Instant.now().toEpochMilli() + "-" + UUID.randomUUID() + extension;
            Path destination = sessionDir.resolve(filename);
            file.transferTo(destination);
            String publicUrl = String.format("/media/%s/%s/%s", sessionId, slot.name().toLowerCase(), filename);
            return new StoredVideo(filename, publicUrl, destination);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to store uploaded video", e);
        }
    }

    private String extractExtension(String original) {
        if (original == null || !original.contains(".")) {
            return "";
        }
        return original.substring(original.lastIndexOf('.'));
    }

    public static class StoredVideo {
        private final String filename;
        private final String publicUrl;
        private final Path path;

        public StoredVideo(String filename, String publicUrl, Path path) {
            this.filename = filename;
            this.publicUrl = publicUrl;
            this.path = path;
        }

        public String getFilename() {
            return filename;
        }

        public String getPublicUrl() {
            return publicUrl;
        }

        public Path getPath() {
            return path;
        }
    }
}
