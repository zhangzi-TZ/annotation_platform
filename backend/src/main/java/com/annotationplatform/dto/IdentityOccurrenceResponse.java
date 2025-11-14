package com.annotationplatform.dto;

import com.annotationplatform.model.VideoSlot;

import java.util.List;

public class IdentityOccurrenceResponse {
    private String id;
    private String videoId;
    private VideoSlot slot;
    private String trackId;
    private List<String> boxIds;
    private List<Integer> frameIndices;

    public IdentityOccurrenceResponse() {
    }

    public IdentityOccurrenceResponse(String id,
                                      String videoId,
                                      VideoSlot slot,
                                      String trackId,
                                      List<String> boxIds,
                                      List<Integer> frameIndices) {
        this.id = id;
        this.videoId = videoId;
        this.slot = slot;
        this.trackId = trackId;
        this.boxIds = boxIds;
        this.frameIndices = frameIndices;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public VideoSlot getSlot() {
        return slot;
    }

    public void setSlot(VideoSlot slot) {
        this.slot = slot;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public List<String> getBoxIds() {
        return boxIds;
    }

    public void setBoxIds(List<String> boxIds) {
        this.boxIds = boxIds;
    }

    public List<Integer> getFrameIndices() {
        return frameIndices;
    }

    public void setFrameIndices(List<Integer> frameIndices) {
        this.frameIndices = frameIndices;
    }
}
