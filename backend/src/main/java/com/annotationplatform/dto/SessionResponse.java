package com.annotationplatform.dto;

import java.time.LocalDateTime;
import java.util.List;

public class SessionResponse {
    private String id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private List<VideoResponse> videos;
    private List<PersonIdentityResponse> identities;

    public SessionResponse() {
    }

    public SessionResponse(String id,
                           String name,
                           String description,
                           LocalDateTime createdAt,
                           List<VideoResponse> videos,
                           List<PersonIdentityResponse> identities) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
        this.videos = videos;
        this.identities = identities;
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

    public List<VideoResponse> getVideos() {
        return videos;
    }

    public void setVideos(List<VideoResponse> videos) {
        this.videos = videos;
    }

    public List<PersonIdentityResponse> getIdentities() {
        return identities;
    }

    public void setIdentities(List<PersonIdentityResponse> identities) {
        this.identities = identities;
    }
}
