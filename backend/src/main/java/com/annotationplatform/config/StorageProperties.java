package com.annotationplatform.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.file.Path;
import java.nio.file.Paths;

@ConfigurationProperties(prefix = "app.storage")
public class StorageProperties {

    private Path root = Paths.get("storage");
    private String videosDir = "videos";
    private String dataDir = "data";
    private String sessionStore = "sessions.json";

    public Path getRoot() {
        return root;
    }

    public void setRoot(Path root) {
        this.root = root;
    }

    public String getVideosDir() {
        return videosDir;
    }

    public void setVideosDir(String videosDir) {
        this.videosDir = videosDir;
    }

    public String getDataDir() {
        return dataDir;
    }

    public void setDataDir(String dataDir) {
        this.dataDir = dataDir;
    }

    public String getSessionStore() {
        return sessionStore;
    }

    public void setSessionStore(String sessionStore) {
        this.sessionStore = sessionStore;
    }

    public Path videosPath() {
        return root.resolve(videosDir);
    }

    public Path dataPath() {
        return root.resolve(dataDir);
    }

    public Path sessionStorePath() {
        return dataPath().resolve(sessionStore);
    }
}
