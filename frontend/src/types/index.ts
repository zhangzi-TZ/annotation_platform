export interface BoundingBox {
  boxId: string;
  frameIndex: number;
  x: number;
  y: number;
  width: number;
  height: number;
  frameWidth: number;
  frameHeight: number;
  trackId: string;
}

export type VideoSlot = 'A' | 'B';

export interface VideoResource {
  id: string;
  slot: VideoSlot;
  originalFilename: string;
  mimeType: string;
  publicUrl: string;
  originalUrl: string;
  boxes: BoundingBox[];
}

export interface VideoUploadResponse {
  id: string;
  slot: VideoSlot;
  originalFilename: string;
  publicUrl: string;
  originalUrl: string;
  boundingBoxCount: number;
}

export interface IdentityOccurrence {
  id: string;
  videoId: string;
  slot: VideoSlot;
  trackId: string;
  boxIds: string[];
  frameIndices: number[];
}

export interface PersonIdentity {
  id: string;
  label: string;
  color: string;
  occurrences: IdentityOccurrence[];
}

export interface AnnotationSession {
  id: string;
  name: string;
  description?: string;
  createdAt: string;
  videos: VideoResource[];
  identities: PersonIdentity[];
}

export interface CreateSessionPayload {
  name: string;
  description?: string;
}

export interface CreateIdentityPayload {
  label: string;
  occurrences: Array<{
    videoId: string;
    trackId: string;
    boxIds: string[];
    frameIndices: number[];
  }>;
}

export interface TrackSummary {
  videoId: string;
  slot: VideoSlot;
  trackId: string;
  boxIds: string[];
  frameIndices: number[];
  boxCount: number;
  firstFrame: number;
  lastFrame: number;
  assignedIdentityId?: string;
  assignedIdentityLabel?: string;
  assignedIdentityColor?: string;
}
