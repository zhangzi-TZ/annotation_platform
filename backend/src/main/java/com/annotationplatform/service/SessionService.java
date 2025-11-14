package com.annotationplatform.service;

import com.annotationplatform.dto.CreateIdentityRequest;
import com.annotationplatform.dto.IdentityOccurrenceRequest;
import com.annotationplatform.dto.UpdateIdentityRequest;
import com.annotationplatform.exception.BadRequestException;
import com.annotationplatform.exception.ResourceNotFoundException;
import com.annotationplatform.model.AnnotationSession;
import com.annotationplatform.model.BoundingBox;
import com.annotationplatform.model.IdentityOccurrence;
import com.annotationplatform.model.PersonIdentity;
import com.annotationplatform.model.VideoAsset;
import com.annotationplatform.model.VideoSlot;
import com.annotationplatform.storage.SessionStorageService;
import com.annotationplatform.storage.VideoStorageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SessionService {

    private final SessionStorageService sessionStorageService;
    private final VideoStorageService videoStorageService;
    private final BoundingBoxParser boundingBoxParser;
    private final ColorPaletteService colorPaletteService;
    private final ObjectMapper objectMapper;
    private final VideoPreviewService videoPreviewService;

    public SessionService(SessionStorageService sessionStorageService,
                          VideoStorageService videoStorageService,
                          BoundingBoxParser boundingBoxParser,
                          ColorPaletteService colorPaletteService,
                          ObjectMapper objectMapper,
                          VideoPreviewService videoPreviewService) {
        this.sessionStorageService = sessionStorageService;
        this.videoStorageService = videoStorageService;
        this.boundingBoxParser = boundingBoxParser;
        this.colorPaletteService = colorPaletteService;
        this.objectMapper = objectMapper;
        this.videoPreviewService = videoPreviewService;
    }

    public AnnotationSession createSession(String name, String description) {
        AnnotationSession session = new AnnotationSession();
        session.setId(UUID.randomUUID().toString());
        session.setName(name);
        session.setDescription(description);
        session.setCreatedAt(LocalDateTime.now());
        sessionStorageService.save(session);
        return session;
    }

    public List<AnnotationSession> getSessions() {
        return sessionStorageService.findAll().stream()
                .sorted(Comparator.comparing(AnnotationSession::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

    public AnnotationSession getSession(String sessionId) {
        return sessionStorageService.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Session %s not found", sessionId)));
    }

    public VideoAsset uploadVideo(String sessionId, String slotLabel, MultipartFile videoFile, MultipartFile boxesFile) {
        if (videoFile == null || videoFile.isEmpty()) {
            throw new BadRequestException("Video file is required");
        }
        VideoSlot slot = VideoSlot.fromLabel(slotLabel);
        AnnotationSession session = getSession(sessionId);

        VideoStorageService.StoredVideo stored = videoStorageService.save(sessionId, slot, videoFile);
        List<BoundingBox> boxes = extractBoxes(boxesFile);

        VideoAsset asset = new VideoAsset();
        asset.setId(slot.name().toLowerCase());
        asset.setSlot(slot);
        asset.setOriginalFilename(videoFile.getOriginalFilename());
        asset.setMimeType(videoFile.getContentType());
        asset.setStoredFilename(stored.getFilename());
        asset.setOriginalPublicUrl(stored.getPublicUrl());
        asset.setPublicUrl(stored.getPublicUrl());
        asset.setBoxes(new ArrayList<>(boxes));

        videoPreviewService.generatePreview(sessionId, slot, stored)
                .ifPresent(preview -> {
                    String previewUrl = buildMediaUrl(sessionId, slot, preview.getFilename());
                    asset.setPreviewFilename(preview.getFilename());
                    asset.setPublicUrl(previewUrl);
                });

        session.getVideos().put(slot, asset);
        sessionStorageService.save(session);
        return asset;
    }

    private String buildMediaUrl(String sessionId, VideoSlot slot, String filename) {
        return String.format("/media/%s/%s/%s", sessionId, slot.name().toLowerCase(), filename);
    }

    private List<BoundingBox> extractBoxes(MultipartFile boxesFile) {
        if (boxesFile == null || boxesFile.isEmpty()) {
            return List.of();
        }
        try {
            String payload = new String(boxesFile.getBytes());
            return boundingBoxParser.parse(payload);
        } catch (IOException e) {
            throw new BadRequestException("Unable to read bounding boxes payload");
        }
    }

    public PersonIdentity createIdentity(String sessionId, CreateIdentityRequest request) {
        AnnotationSession session = getSession(sessionId);
        String label = request.getLabel().trim();
        ensureLabelUnique(session, label, null);

        PersonIdentity identity = new PersonIdentity();
        identity.setId(UUID.randomUUID().toString());
        identity.setLabel(label);
        identity.setColor(colorPaletteService.nextColor());
        identity.setOccurrences(new ArrayList<>());

        identity.getOccurrences().addAll(resolveOccurrences(session, request.getOccurrences()));
        session.getIdentities().add(identity);
        sessionStorageService.save(session);
        return identity;
    }

    public PersonIdentity updateIdentity(String sessionId, String identityId, UpdateIdentityRequest request) {
        AnnotationSession session = getSession(sessionId);
        PersonIdentity identity = findIdentity(session, identityId);
        if (StringUtils.hasText(request.getLabel())) {
            ensureLabelUnique(session, request.getLabel().trim(), identityId);
            identity.setLabel(request.getLabel().trim());
        }
        if (request.getOccurrences() != null) {
            identity.setOccurrences(new ArrayList<>(resolveOccurrences(session, request.getOccurrences())));
        }
        sessionStorageService.save(session);
        return identity;
    }

    public void deleteIdentity(String sessionId, String identityId) {
        AnnotationSession session = getSession(sessionId);
        boolean removed = session.getIdentities().removeIf(identity -> identity.getId().equals(identityId));
        if (!removed) {
            throw new ResourceNotFoundException(String.format("Identity %s not found", identityId));
        }
        sessionStorageService.save(session);
    }

    public byte[] exportSession(String sessionId) {
        AnnotationSession session = getSession(sessionId);
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(session);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to export session", e);
        }
    }

    private void ensureLabelUnique(AnnotationSession session, String label, String ignoreId) {
        boolean exists = session.getIdentities().stream()
                .anyMatch(identity -> identity.getLabel().equalsIgnoreCase(label)
                        && (ignoreId == null || !identity.getId().equals(ignoreId)));
        if (exists) {
            throw new BadRequestException("Identity label already exists: " + label);
        }
    }

    private List<IdentityOccurrence> resolveOccurrences(AnnotationSession session, List<IdentityOccurrenceRequest> requests) {
        return requests.stream()
                .map(req -> toOccurrence(session, req))
                .collect(Collectors.toList());
    }

    private IdentityOccurrence toOccurrence(AnnotationSession session, IdentityOccurrenceRequest req) {
        VideoAsset video = findVideo(session, req.getVideoId());
        if (video.getBoxes() == null || video.getBoxes().isEmpty()) {
            throw new BadRequestException("Video " + video.getId() + " does not contain any bounding boxes");
        }
        Map<String, BoundingBox> boxIndex = video.getBoxes().stream()
                .collect(Collectors.toMap(BoundingBox::getBoxId, b -> b));
        if (!boxIndex.keySet().containsAll(req.getBoxIds())) {
            throw new BadRequestException("One or more bounding box ids are invalid for video " + video.getId());
        }
        IdentityOccurrence occurrence = new IdentityOccurrence();
        occurrence.setId(UUID.randomUUID().toString());
        occurrence.setVideoId(video.getId());
        occurrence.setSlot(video.getSlot());
        occurrence.setTrackId(req.getTrackId());
        occurrence.setBoxIds(new ArrayList<>(req.getBoxIds()));
        occurrence.setFrameIndices(new ArrayList<>(Optional.ofNullable(req.getFrameIndices()).orElse(List.of())));
        return occurrence;
    }

    private VideoAsset findVideo(AnnotationSession session, String videoId) {
        return session.getVideos().values().stream()
                .filter(video -> video.getId().equals(videoId))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("Video " + videoId + " not found"));
    }

    private PersonIdentity findIdentity(AnnotationSession session, String identityId) {
        return session.getIdentities().stream()
                .filter(identity -> identity.getId().equals(identityId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Identity %s not found", identityId)));
    }
}
