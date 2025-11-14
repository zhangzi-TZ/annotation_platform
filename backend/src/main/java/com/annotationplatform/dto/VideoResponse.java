package com.annotationplatform.dto;

import com.annotationplatform.model.VideoSlot;

import java.util.List;

public class VideoResponse {
    private String id;
    private VideoSlot slot;
    private String originalFilename;
    private String mimeType;
    private String publicUrl;
    private String originalUrl;
    private List<BoundingBoxResponse> boxes;

    public VideoResponse() {
    }

    public VideoResponse(String id,
                         VideoSlot slot,
                         String originalFilename,
                         String mimeType,
                         String publicUrl,
                         String originalUrl,
                         List<BoundingBoxResponse> boxes) {
        this.id = id;
        this.slot = slot;
        this.originalFilename = originalFilename;
        this.mimeType = mimeType;
        this.publicUrl = publicUrl;
        this.originalUrl = originalUrl;
        this.boxes = boxes;
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

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
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

    public List<BoundingBoxResponse> getBoxes() {
        return boxes;
    }

    public void setBoxes(List<BoundingBoxResponse> boxes) {
        this.boxes = boxes;
    }
}
