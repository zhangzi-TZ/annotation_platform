package com.annotationplatform.model;

public enum VideoSlot {
    A,
    B;

    public static VideoSlot fromLabel(String label) {
        if (label == null) {
            throw new IllegalArgumentException("Video slot is required");
        }
        String upper = label.trim().toUpperCase();
        switch (upper) {
            case "A":
            case "VIDEO_A":
            case "FIRST":
                return A;
            case "B":
            case "VIDEO_B":
            case "SECOND":
                return B;
            default:
                throw new IllegalArgumentException("Unsupported video slot: " + label);
        }
    }
}
