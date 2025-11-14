package com.annotationplatform.storage;

import com.annotationplatform.config.StorageProperties;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class StorageInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(StorageInitializer.class);

    private final StorageProperties storageProperties;

    public StorageInitializer(StorageProperties storageProperties) {
        this.storageProperties = storageProperties;
    }

    @PostConstruct
    public void prepareDirectories() {
        createIfMissing(storageProperties.getRoot());
        createIfMissing(storageProperties.videosPath());
        createIfMissing(storageProperties.dataPath());
        Path sessionsFile = storageProperties.sessionStorePath();
        if (Files.notExists(sessionsFile)) {
            try {
                Files.createFile(sessionsFile);
                Files.writeString(sessionsFile, "[]");
            } catch (IOException e) {
                LOGGER.error("Failed to initialize session store", e);
            }
        }
    }

    private void createIfMissing(Path path) {
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            LOGGER.error("Failed to create directory {}", path, e);
        }
    }
}
