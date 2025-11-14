package com.annotationplatform.controller;

import com.annotationplatform.dto.BoundingBoxResponse;
import com.annotationplatform.dto.CreateIdentityRequest;
import com.annotationplatform.dto.CreateSessionRequest;
import com.annotationplatform.dto.IdentityOccurrenceResponse;
import com.annotationplatform.dto.PersonIdentityResponse;
import com.annotationplatform.dto.SessionResponse;
import com.annotationplatform.dto.VideoResponse;
import com.annotationplatform.dto.VideoUploadResponse;
import com.annotationplatform.model.AnnotationSession;
import com.annotationplatform.model.BoundingBox;
import com.annotationplatform.model.IdentityOccurrence;
import com.annotationplatform.model.PersonIdentity;
import com.annotationplatform.model.VideoAsset;
import com.annotationplatform.service.SessionService;
import javax.validation.Valid;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping
    public SessionResponse createSession(@Valid @RequestBody CreateSessionRequest request) {
        AnnotationSession session = sessionService.createSession(request.getName(), request.getDescription());
        return toResponse(session);
    }

    @GetMapping
    public List<SessionResponse> getSessions() {
        return sessionService.getSessions().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{sessionId}")
    public SessionResponse getSession(@PathVariable String sessionId) {
        return toResponse(sessionService.getSession(sessionId));
    }

    @PostMapping(value = "/{sessionId}/videos/{slot}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public VideoUploadResponse uploadVideo(@PathVariable String sessionId,
                                           @PathVariable String slot,
                                           @RequestParam("video") MultipartFile video,
                                           @RequestParam(value = "boxes", required = false) MultipartFile boxes) {
        VideoAsset asset = sessionService.uploadVideo(sessionId, slot, video, boxes);
        return new VideoUploadResponse(
                asset.getId(),
                asset.getSlot(),
                asset.getOriginalFilename(),
                asset.getPublicUrl(),
                asset.getOriginalPublicUrl(),
                asset.getBoxes().size()
        );
    }

    @PostMapping("/{sessionId}/identities")
    public PersonIdentityResponse createIdentity(@PathVariable String sessionId,
                                                 @Valid @RequestBody CreateIdentityRequest request) {
        PersonIdentity identity = sessionService.createIdentity(sessionId, request);
        return toResponse(identity);
    }

    @PutMapping("/{sessionId}/identities/{identityId}")
    public PersonIdentityResponse updateIdentity(@PathVariable String sessionId,
                                                 @PathVariable String identityId,
                                                 @Valid @RequestBody com.annotationplatform.dto.UpdateIdentityRequest request) {
        PersonIdentity identity = sessionService.updateIdentity(sessionId, identityId, request);
        return toResponse(identity);
    }

    @DeleteMapping("/{sessionId}/identities/{identityId}")
    public ResponseEntity<Void> deleteIdentity(@PathVariable String sessionId, @PathVariable String identityId) {
        sessionService.deleteIdentity(sessionId, identityId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{sessionId}/export")
    public ResponseEntity<Resource> exportSession(@PathVariable String sessionId) {
        byte[] payload = sessionService.exportSession(sessionId);
        Resource resource = new ByteArrayResource(payload);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"session-" + sessionId + ".json\"")
                .contentLength(payload.length)
                .body(resource);
    }

    private SessionResponse toResponse(AnnotationSession session) {
        List<VideoResponse> videos = session.getVideos().values().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        List<PersonIdentityResponse> identities = session.getIdentities().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return new SessionResponse(
                session.getId(),
                session.getName(),
                session.getDescription(),
                session.getCreatedAt(),
                videos,
                identities
        );
    }

    private VideoResponse toResponse(VideoAsset asset) {
        List<BoundingBoxResponse> boxes = asset.getBoxes().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return new VideoResponse(
                asset.getId(),
                asset.getSlot(),
                asset.getOriginalFilename(),
                asset.getMimeType(),
                asset.getPublicUrl(),
                asset.getOriginalPublicUrl(),
                boxes
        );
    }

    private BoundingBoxResponse toResponse(BoundingBox box) {
        return new BoundingBoxResponse(
                box.getBoxId(),
                box.getFrameIndex(),
                box.getX(),
                box.getY(),
                box.getWidth(),
                box.getHeight(),
                box.getFrameWidth(),
                box.getFrameHeight(),
                box.getTrackId()
        );
    }

    private PersonIdentityResponse toResponse(PersonIdentity identity) {
        List<IdentityOccurrenceResponse> occurrences = identity.getOccurrences().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return new PersonIdentityResponse(
                identity.getId(),
                identity.getLabel(),
                identity.getColor(),
                occurrences
        );
    }

    private IdentityOccurrenceResponse toResponse(IdentityOccurrence occurrence) {
        return new IdentityOccurrenceResponse(
                occurrence.getId(),
                occurrence.getVideoId(),
                occurrence.getSlot(),
                occurrence.getTrackId(),
                occurrence.getBoxIds(),
                occurrence.getFrameIndices()
        );
    }
}
