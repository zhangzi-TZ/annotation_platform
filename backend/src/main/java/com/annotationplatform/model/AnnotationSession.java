package com.annotationplatform.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class AnnotationSession {
    private String id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private Map<VideoSlot, VideoAsset> videos;
    private List<PersonIdentity> identities;

    public AnnotationSession() {
        this.videos = new EnumMap<>(VideoSlot.class);
        this.identities = new ArrayList<>();
    }

    public AnnotationSession(String id,
                             String name,
                             String description,
                             LocalDateTime createdAt,
                             Map<VideoSlot, VideoAsset> videos,
                             List<PersonIdentity> identities) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
        setVideos(videos);
        setIdentities(identities);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Map<VideoSlot, VideoAsset> getVideos() {
        if (videos == null) {
            videos = new EnumMap<>(VideoSlot.class);
        }
        return videos;
    }

    public void setVideos(Map<VideoSlot, VideoAsset> videos) {
        this.videos = videos != null ? videos : new EnumMap<>(VideoSlot.class);
    }

    public List<PersonIdentity> getIdentities() {
        if (identities == null) {
            identities = new ArrayList<>();
        }
        return identities;
    }

    public void setIdentities(List<PersonIdentity> identities) {
        this.identities = identities != null ? identities : new ArrayList<>();
    }
}
