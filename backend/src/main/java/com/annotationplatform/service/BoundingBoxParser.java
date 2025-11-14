package com.annotationplatform.service;

import com.annotationplatform.model.BoundingBox;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Component
public class BoundingBoxParser {

    private static final TypeReference<List<BoundingBox>> BOX_LIST_TYPE = new TypeReference<>() {};

    private final ObjectMapper objectMapper;

    public BoundingBoxParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<BoundingBox> parse(String payload) {
        if (payload == null || payload.isBlank()) {
            return Collections.emptyList();
        }
        try {
            List<BoundingBox> boxes = objectMapper.readValue(payload, BOX_LIST_TYPE);
            boxes.forEach(box -> {
                if (box.getBoxId() == null || box.getBoxId().isBlank()) {
                    box.setBoxId(UUID.randomUUID().toString());
                }
            });
            return boxes;
        } catch (IOException ex) {
            throw new IllegalArgumentException("Invalid bounding box payload: " + ex.getMessage(), ex);
        }
    }
}
