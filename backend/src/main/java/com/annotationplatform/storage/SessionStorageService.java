package com.annotationplatform.storage;

import com.annotationplatform.config.StorageProperties;
import com.annotationplatform.model.AnnotationSession;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionStorageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SessionStorageService.class);
    private static final TypeReference<List<AnnotationSession>> SESSION_LIST = new TypeReference<>() {};

    private final StorageProperties storageProperties;
    private final ObjectMapper objectMapper;
    private final Map<String, AnnotationSession> sessionIndex = new ConcurrentHashMap<>();

    public SessionStorageService(StorageProperties storageProperties, ObjectMapper objectMapper) {
        this.storageProperties = storageProperties;
        this.objectMapper = objectMapper;
        loadFromDisk();
    }

    public List<AnnotationSession> findAll() {
        return new ArrayList<>(sessionIndex.values());
    }

    public Optional<AnnotationSession> findById(String id) {
        return Optional.ofNullable(sessionIndex.get(id));
    }

    public synchronized AnnotationSession save(AnnotationSession session) {
        sessionIndex.put(session.getId(), session);
        persist();
        return session;
    }

    public synchronized void delete(String id) {
        sessionIndex.remove(id);
        persist();
    }

    private void loadFromDisk() {
        Path path = storageProperties.sessionStorePath();
        if (Files.notExists(path)) {
            return;
        }
        try {
            byte[] content = Files.readAllBytes(path);
            if (content.length == 0) {
                return;
            }
            List<AnnotationSession> sessions = objectMapper.readValue(content, SESSION_LIST);
            sessions.stream()
                    .filter(session -> session.getId() != null)
                    .forEach(session -> {
                        normalize(session);
                        sessionIndex.put(session.getId(), session);
                    });
        } catch (IOException e) {
            LOGGER.error("Failed to load session store", e);
        }
    }

    private void persist() {
        Path path = storageProperties.sessionStorePath();
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(path.toFile(), sessionIndex.values());
        } catch (IOException e) {
            LOGGER.error("Failed to persist session store", e);
        }
    }

    private void normalize(AnnotationSession session) {
        if (session.getVideos() == null) {
            session.setVideos(new java.util.EnumMap<>(com.annotationplatform.model.VideoSlot.class));
        }
        if (session.getIdentities() == null) {
            session.setIdentities(new ArrayList<>());
        }
        session.getVideos().values().forEach(video -> {
            if (video.getBoxes() == null) {
                video.setBoxes(new ArrayList<>());
            }
            if (video.getOriginalPublicUrl() == null) {
                video.setOriginalPublicUrl(video.getPublicUrl());
            }
        });
        session.getIdentities().forEach(identity -> {
            if (identity.getOccurrences() == null) {
                identity.setOccurrences(new ArrayList<>());
            }
            identity.getOccurrences().forEach(occurrence -> {
                if (occurrence.getBoxIds() == null) {
                    occurrence.setBoxIds(new ArrayList<>());
                }
                if (occurrence.getFrameIndices() == null) {
                    occurrence.setFrameIndices(new ArrayList<>());
                }
            });
        });
    }
}
