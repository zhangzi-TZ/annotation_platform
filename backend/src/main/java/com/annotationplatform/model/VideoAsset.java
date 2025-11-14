package com.annotationplatform.model;

import java.util.ArrayList;
import java.util.List;

public class VideoAsset {
    private String id;
    private VideoSlot slot;
    private String originalFilename;
    private String mimeType;
    private String storedFilename;
    private String publicUrl;
    private String originalPublicUrl;
    private String previewFilename;
    private List<BoundingBox> boxes;

    public VideoAsset() {
        this.boxes = new ArrayList<>();
    }

    public VideoAsset(String id,
                      VideoSlot slot,
                      String originalFilename,
                      String mimeType,
                      String storedFilename,
                      String publicUrl,
                      String originalPublicUrl,
                      String previewFilename,
                      List<BoundingBox> boxes) {
        this.id = id;
        this.slot = slot;
        this.originalFilename = originalFilename;
        this.mimeType = mimeType;
        this.storedFilename = storedFilename;
        this.publicUrl = publicUrl;
        this.originalPublicUrl = originalPublicUrl;
        this.previewFilename = previewFilename;
        setBoxes(boxes);
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

    public String getStoredFilename() {
        return storedFilename;
    }

    public void setStoredFilename(String storedFilename) {
        this.storedFilename = storedFilename;
    }

    public String getPublicUrl() {
        return publicUrl;
    }

    public void setPublicUrl(String publicUrl) {
        this.publicUrl = publicUrl;
    }

    public String getOriginalPublicUrl() {
        return originalPublicUrl;
    }

    public void setOriginalPublicUrl(String originalPublicUrl) {
        this.originalPublicUrl = originalPublicUrl;
    }

    public String getPreviewFilename() {
        return previewFilename;
    }

    public void setPreviewFilename(String previewFilename) {
        this.previewFilename = previewFilename;
    }

    public List<BoundingBox> getBoxes() {
        if (boxes == null) {
            boxes = new ArrayList<>();
        }
        return boxes;
    }

    public void setBoxes(List<BoundingBox> boxes) {
        this.boxes = boxes != null ? boxes : new ArrayList<>();
    }
}
