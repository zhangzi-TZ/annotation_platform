package com.annotationplatform.dto;

import com.annotationplatform.model.VideoSlot;

public class VideoUploadResponse {
    private String id;
    private VideoSlot slot;
    private String originalFilename;
    private String publicUrl;
    private String originalUrl;
    private int boundingBoxCount;

    public VideoUploadResponse() {
    }

    public VideoUploadResponse(String id,
                               VideoSlot slot,
                               String originalFilename,
                               String publicUrl,
                               String originalUrl,
                               int boundingBoxCount) {
        this.id = id;
        this.slot = slot;
        this.originalFilename = originalFilename;
        this.publicUrl = publicUrl;
        this.originalUrl = originalUrl;
        this.boundingBoxCount = boundingBoxCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public VideoSlot getSlot() {
        return slot;
    }

    public void setSlot(VideoSlot slot) {
        this.slot = slot;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getPublicUrl() {
        return publicUrl;
    }

    public void setPublicUrl(String publicUrl) {
        this.publicUrl = publicUrl;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public int getBoundingBoxCount() {
        return boundingBoxCount;
    }

    public void setBoundingBoxCount(int boundingBoxCount) {
        this.boundingBoxCount = boundingBoxCount;
    }
}
