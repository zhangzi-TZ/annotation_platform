import { ChangeEvent, FormEvent, useEffect, useMemo, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { SessionApi } from '../api/sessionApi';
import {
  AnnotationSession,
  PersonIdentity,
  TrackSummary,
  VideoResource
} from '../types';
import { API_BASE_URL } from '../config';

const SessionDetailPage = () => {
  const { sessionId } = useParams();
  const [session, setSession] = useState<AnnotationSession | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [selectedTracks, setSelectedTracks] = useState<Record<string, TrackSummary | undefined>>({});
  const [labelInput, setLabelInput] = useState('');
  const [busy, setBusy] = useState(false);

  useEffect(() => {
    if (!sessionId) return;
    const load = async () => {
      try {
        setLoading(true);
        const data = await SessionApi.getSession(sessionId);
        setSession(data);
      } catch (err) {
        setError((err as Error).message);
      } finally {
        setLoading(false);
      }
    };
    load();
  }, [sessionId]);

  const trackSummaries = useMemo(() => buildTrackSummary(session), [session]);

  const handleVideoUpload = async (
    slot: 'a' | 'b',
    payload: { video: File; boxes?: File }
  ) => {
    if (!sessionId) return;
    try {
      setBusy(true);
      await SessionApi.uploadVideo(sessionId, slot, payload);
      const data = await SessionApi.getSession(sessionId);
      setSession(data);
    } catch (err) {
      setError((err as Error).message);
    } finally {
      setBusy(false);
    }
  };

  const toggleTrack = (summary: TrackSummary) => {
    setSelectedTracks((prev) => {
      const current = prev[summary.videoId];
      if (current && current.trackId === summary.trackId) {
        const next = { ...prev };
        delete next[summary.videoId];
        return next;
      }
      return { ...prev, [summary.videoId]: summary };
    });
  };

  const resetSelection = () => {
    setSelectedTracks({});
    setLabelInput('');
  };

  const selectedTrackList = Object.values(selectedTracks).filter(Boolean) as TrackSummary[];

  const occurrencePayloads = selectedTrackList.map((track) => ({
    videoId: track.videoId,
    trackId: track.trackId,
    boxIds: track.boxIds,
    frameIndices: track.frameIndices
  }));

  const handleCreateIdentity = async (event: FormEvent) => {
    event.preventDefault();
    if (!sessionId || !labelInput.trim() || occurrencePayloads.length === 0) {
      return;
    }
    try {
      setBusy(true);
      await SessionApi.createIdentity(sessionId, {
        label: labelInput.trim(),
        occurrences: occurrencePayloads
      });
      const data = await SessionApi.getSession(sessionId);
      setSession(data);
      resetSelection();
    } catch (err) {
      setError((err as Error).message);
    } finally {
      setBusy(false);
    }
  };

  const handleAttachToIdentity = async (identity: PersonIdentity) => {
    if (!sessionId || occurrencePayloads.length === 0) {
      return;
    }
    try {
      setBusy(true);
      await SessionApi.updateIdentity(sessionId, identity.id, {
        label: identity.label,
        occurrences: [
          ...identity.occurrences.map((occ) => ({
            videoId: occ.videoId,
            trackId: occ.trackId,
            boxIds: occ.boxIds,
            frameIndices: occ.frameIndices
          })),
          ...occurrencePayloads
        ]
      });
      const data = await SessionApi.getSession(sessionId);
      setSession(data);
      resetSelection();
    } catch (err) {
      setError((err as Error).message);
    } finally {
      setBusy(false);
    }
  };

  const handleDeleteIdentity = async (identity: PersonIdentity) => {
    if (!sessionId) return;
    if (!window.confirm(`Delete identity ${identity.label}?`)) {
      return;
    }
    try {
      setBusy(true);
      await SessionApi.deleteIdentity(sessionId, identity.id);
      const data = await SessionApi.getSession(sessionId);
      setSession(data);
    } catch (err) {
      setError((err as Error).message);
    } finally {
      setBusy(false);
    }
  };

  const handleExport = async () => {
    if (!sessionId) return;
    const blob = await SessionApi.exportSession(sessionId);
    const url = URL.createObjectURL(blob);
    const anchor = document.createElement('a');
    anchor.href = url;
    anchor.download = `session-${sessionId}.json`;
    anchor.click();
    URL.revokeObjectURL(url);
  };

  if (loading) {
    return (
      <div className="page">
        <p>Loading session…</p>
      </div>
    );
  }

  if (!session) {
    return (
      <div className="page">
        <p className="error">Unable to load session.</p>
        <Link to="/">Back to sessions</Link>
      </div>
    );
  }

  const videoA = session.videos.find((video) => video.slot === 'A');
  const videoB = session.videos.find((video) => video.slot === 'B');
  const tracksA = trackSummaries[videoA?.id ?? ''] ?? [];
  const tracksB = trackSummaries[videoB?.id ?? ''] ?? [];

  return (
    <div className="page">
      <header className="page__header">
        <div>
          <p className="breadcrumb"><Link to="/">← All sessions</Link></p>
          <h1>{session.name}</h1>
          {session.description && <p className="subtitle">{session.description}</p>}
        </div>
        <div className="header-actions">
          <button onClick={handleExport}>Export JSON</button>
        </div>
      </header>

      {error && <p className="error">{error}</p>}

      <section className="upload-grid">
        <VideoUploadPanel
          slot="A"
          video={videoA}
          onUpload={(files) => handleVideoUpload('a', files)}
        />
        <VideoUploadPanel
          slot="B"
          video={videoB}
          onUpload={(files) => handleVideoUpload('b', files)}
        />
      </section>

      <section className="workspace">
        <div className="video-column">
          <VideoPreview video={videoA} title="Video A" />
          <TrackList
            title="Tracks in Video A"
            tracks={tracksA}
            selected={selectedTracks[videoA?.id ?? '']?.trackId}
            onSelect={toggleTrack}
          />
        </div>
        <div className="video-column">
          <VideoPreview video={videoB} title="Video B" />
          <TrackList
            title="Tracks in Video B"
            tracks={tracksB}
            selected={selectedTracks[videoB?.id ?? '']?.trackId}
            onSelect={toggleTrack}
          />
        </div>
      </section>

      <section className="card">
        <form className="match-form" onSubmit={handleCreateIdentity}>
          <div>
            <h3>Selected tracks</h3>
            {selectedTrackList.length === 0 && <p className="muted">Select tracks from each video to match identities.</p>}
            <ul className="pill-list">
              {selectedTrackList.map((track) => (
                <li key={track.videoId + track.trackId}>{track.slot} · {track.trackId} ({track.boxCount} boxes)</li>
              ))}
            </ul>
          </div>
          <div className="form-inline">
            <input
              value={labelInput}
              onChange={(event) => setLabelInput(event.target.value)}
              placeholder="New identity label"
              disabled={busy}
            />
            <button type="submit" disabled={busy || selectedTrackList.length === 0 || !labelInput.trim()}>
              Create identity
            </button>
            <button type="button" className="ghost" onClick={resetSelection} disabled={busy}>
              Reset
            </button>
          </div>
          <div>
            <label>Attach to existing identity</label>
            <div className="identity-options">
              {session.identities.map((identity) => (
                <button
                  key={identity.id}
                  type="button"
                  className="identity-chip"
                  style={{ borderColor: identity.color, color: identity.color }}
                  onClick={() => handleAttachToIdentity(identity)}
                  disabled={busy || occurrencePayloads.length === 0}
                >
                  {identity.label}
                </button>
              ))}
              {session.identities.length === 0 && <p className="muted">No identities yet.</p>}
            </div>
          </div>
        </form>
      </section>

      <section>
        <h2>Identities</h2>
        <div className="identity-grid">
          {session.identities.map((identity) => (
            <article key={identity.id} className="identity-card" style={{ borderColor: identity.color }}>
              <header>
                <h3>{identity.label}</h3>
                <button className="ghost" onClick={() => handleDeleteIdentity(identity)} disabled={busy}>Delete</button>
              </header>
              <ul>
                {identity.occurrences.map((occ) => (
                  <li key={occ.id}>{occ.slot} · {occ.trackId} ({occ.boxIds.length} boxes)</li>
                ))}
              </ul>
            </article>
          ))}
          {session.identities.length === 0 && <p className="muted">No identities defined yet.</p>}
        </div>
      </section>
    </div>
  );
};

export default SessionDetailPage;

interface VideoUploadPanelProps {
  slot: 'A' | 'B';
  video?: VideoResource;
  onUpload: (payload: { video: File; boxes?: File }) => void;
}

const VideoUploadPanel = ({ slot, video, onUpload }: VideoUploadPanelProps) => {
  const [videoFile, setVideoFile] = useState<File | null>(null);
  const [boxesFile, setBoxesFile] = useState<File | null>(null);

  const handleSubmit = (event: FormEvent) => {
    event.preventDefault();
    if (!videoFile) {
      return;
    }
    onUpload({ video: videoFile, boxes: boxesFile ?? undefined });
    setVideoFile(null);
    setBoxesFile(null);
    (event.target as HTMLFormElement).reset();
  };

  const handleFileChange = (setter: (value: File | null) => void) => (event: ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0] ?? null;
    setter(file);
  };

  return (
    <article className="card">
      <h3>Video {slot}</h3>
      {video ? (
        <p className="muted">{video.originalFilename} · {video.boxes.length} bounding boxes</p>
      ) : (
        <p className="muted">No upload yet.</p>
      )}
      <form className="upload-form" onSubmit={handleSubmit}>
        <label>
          <span>Video file</span>
          <input type="file" accept="video/*" onChange={handleFileChange(setVideoFile)} required />
        </label>
        <label>
          <span>Bounding boxes JSON</span>
          <input type="file" accept="application/json" onChange={handleFileChange(setBoxesFile)} />
        </label>
        <button type="submit" disabled={!videoFile}>Upload</button>
      </form>
    </article>
  );
};

interface VideoPreviewProps {
  video?: VideoResource;
  title: string;
}

const VideoPreview = ({ video, title }: VideoPreviewProps) => {
  const videoUrl = video ? `${API_BASE_URL}${video.publicUrl}` : null;
  
  return (
    <div className="video-preview card">
      <h3>{title}</h3>
      {video ? (
        <>
          <video 
            controls 
            src={videoUrl || undefined}
            onError={(e) => {
              console.error('Video load error:', e);
              console.error('Video URL:', videoUrl);
            }}
            onLoadStart={() => {
              console.log('Video loading:', videoUrl);
            }}
          >
            Your browser does not support the video tag.
          </video>
          {videoUrl && (
            <p className="muted" style={{ fontSize: '0.8rem', marginTop: '0.5rem' }}>
              Preview URL: {videoUrl}
            </p>
          )}
          {video?.originalUrl && video.originalUrl !== video.publicUrl && (
            <p className="muted" style={{ fontSize: '0.8rem' }}>
              Original:{' '}
              <a href={`${API_BASE_URL}${video.originalUrl}`} target="_blank" rel="noreferrer">
                download
              </a>
            </p>
          )}
        </>
      ) : (
        <p className="muted">Upload a video to preview.</p>
      )}
    </div>
  );
};

interface TrackListProps {
  title: string;
  tracks: TrackSummary[];
  selected?: string;
  onSelect: (track: TrackSummary) => void;
}

const TrackList = ({ title, tracks, selected, onSelect }: TrackListProps) => (
  <div className="track-list card">
    <h3>{title}</h3>
    {tracks.length === 0 && <p className="muted">No bounding boxes uploaded yet.</p>}
    <ul>
      {tracks.map((track) => (
        <li key={`${track.videoId}-${track.trackId}`}>
          <button
            type="button"
            className={`track-chip ${selected === track.trackId ? 'track-chip--selected' : ''}`}
            onClick={() => onSelect(track)}
            style={{ borderColor: track.assignedIdentityColor ?? '#cbd5f5', color: track.assignedIdentityColor ?? '#1e293b' }}
          >
            {track.slot} · {track.trackId}
            <span className="muted">{track.boxCount} boxes</span>
            {track.assignedIdentityLabel && <span className="tag">{track.assignedIdentityLabel}</span>}
          </button>
        </li>
      ))}
    </ul>
  </div>
);

function buildTrackSummary(session: AnnotationSession | null) {
  if (!session) {
    return {} as Record<string, TrackSummary[]>;
  }
  const assignments = new Map<string, { identityId: string; label: string; color: string }>();
  session.identities.forEach((identity) => {
    identity.occurrences.forEach((occ) => {
      assignments.set(`${occ.videoId}__${occ.trackId}`, {
        identityId: identity.id,
        label: identity.label,
        color: identity.color
      });
    });
  });

  const summary: Record<string, TrackSummary[]> = {};
  session.videos.forEach((video) => {
    const trackAccumulator = new Map<string, { boxIds: string[]; frames: number[]; min: number; max: number }>();
    video.boxes.forEach((box) => {
      const trackId = box.trackId || box.boxId;
      if (!trackAccumulator.has(trackId)) {
        trackAccumulator.set(trackId, { boxIds: [], frames: [], min: box.frameIndex, max: box.frameIndex });
      }
      const acc = trackAccumulator.get(trackId)!;
      acc.boxIds.push(box.boxId);
      acc.frames.push(box.frameIndex);
      acc.min = Math.min(acc.min, box.frameIndex);
      acc.max = Math.max(acc.max, box.frameIndex);
    });

    summary[video.id] = Array.from(trackAccumulator.entries()).map(([trackId, acc]) => {
      const assignment = assignments.get(`${video.id}__${trackId}`);
      return {
        videoId: video.id,
        slot: video.slot,
        trackId,
        boxIds: acc.boxIds,
        frameIndices: acc.frames,
        boxCount: acc.boxIds.length,
        firstFrame: acc.min,
        lastFrame: acc.max,
        assignedIdentityId: assignment?.identityId,
        assignedIdentityLabel: assignment?.label,
        assignedIdentityColor: assignment?.color
      } satisfies TrackSummary;
    });
  });
  return summary;
}
