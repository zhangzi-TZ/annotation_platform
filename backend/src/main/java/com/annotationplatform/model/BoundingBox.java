package com.annotationplatform.model;

public class BoundingBox {
    private String boxId;
    private int frameIndex;
    private double x;
    private double y;
    private double width;
    private double height;
    private double frameWidth;
    private double frameHeight;
    private String trackId;

    public BoundingBox() {
    }

    public BoundingBox(String boxId,
                       int frameIndex,
                       double x,
                       double y,
                       double width,
                       double height,
                       double frameWidth,
                       double frameHeight,
                       String trackId) {
        this.boxId = boxId;
        this.frameIndex = frameIndex;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.trackId = trackId;
    }

    public String getBoxId() {
        return boxId;
    }

    public void setBoxId(String boxId) {
        this.boxId = boxId;
    }

    public int getFrameIndex() {
        return frameIndex;
    }

    public void setFrameIndex(int frameIndex) {
        this.frameIndex = frameIndex;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getFrameWidth() {
        return frameWidth;
    }

    public void setFrameWidth(double frameWidth) {
        this.frameWidth = frameWidth;
    }

    public double getFrameHeight() {
        return frameHeight;
    }

    public void setFrameHeight(double frameHeight) {
        this.frameHeight = frameHeight;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }
}
