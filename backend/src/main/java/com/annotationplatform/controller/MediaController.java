package com.annotationplatform.controller;

import com.annotationplatform.config.StorageProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/media")
@CrossOrigin(originPatterns = {"http://localhost:5173", "https://*.netlify.app", "https://*.pages.dev"})
public class MediaController {

    private static final Logger log = LoggerFactory.getLogger(MediaController.class);

    private final StorageProperties storageProperties;

    public MediaController(StorageProperties storageProperties) {
        this.storageProperties = storageProperties;
    }

    @GetMapping("/{sessionId}/{slot}/{filename:.+}")
    public ResponseEntity<Resource> serveVideo(
            @PathVariable String sessionId,
            @PathVariable String slot,
            @PathVariable String filename) {
        try {
            Path videoPath = storageProperties.videosPath()
                    .resolve(sessionId)
                    .resolve(slot.toLowerCase())
                    .resolve(filename);
            log.info("Serving media sessionId={}, slot={}, filename={}", sessionId, slot, filename);
            if (!Files.exists(videoPath) || !Files.isRegularFile(videoPath)) {
                log.warn("Media not found at {}", videoPath);
                return ResponseEntity.notFound().build();
            }
            
            Resource resource = new FileSystemResource(videoPath);
            String contentType = determineContentType(filename);
            
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                    .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                    .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*")
                    .header(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET, OPTIONS")
                    .header(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "*")
                    .body(resource);
        } catch (Exception e) {
            log.error("Failed to serve media {}/{}/{}", sessionId, slot, filename, e);
            return ResponseEntity.notFound().build();
        }
    }

    private String determineContentType(String filename) {
        String lower = filename.toLowerCase();
        if (lower.endsWith(".mp4")) {
            return "video/mp4";
        } else if (lower.endsWith(".webm")) {
            return "video/webm";
        } else if (lower.endsWith(".mov")) {
            return "video/quicktime";
        } else if (lower.endsWith(".avi")) {
            return "video/x-msvideo";
        } else {
            return "application/octet-stream";
        }
    }
}

