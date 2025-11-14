package com.annotationplatform.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

public class IdentityOccurrenceRequest {
    @NotBlank(message = "videoId is required")
    private String videoId;

    @NotBlank(message = "trackId is required")
    private String trackId;

    @NotEmpty(message = "boxIds cannot be empty")
    private List<String> boxIds;

    private List<Integer> frameIndices;

    public IdentityOccurrenceRequest() {
    }

    public IdentityOccurrenceRequest(String videoId,
                                     String trackId,
                                     List<String> boxIds,
                                     List<Integer> frameIndices) {
        this.videoId = videoId;
        this.trackId = trackId;
        this.boxIds = boxIds;
        this.frameIndices = frameIndices;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
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
