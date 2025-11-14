import { FormEvent, useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { SessionApi } from '../api/sessionApi';
import { AnnotationSession } from '../types';

const SessionListPage = () => {
  const [sessions, setSessions] = useState<AnnotationSession[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [creating, setCreating] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    const load = async () => {
      try {
        const data = await SessionApi.getSessions();
        setSessions(data);
        setError(null);
      } catch (err: any) {
        const message = err?.response?.data?.message || err?.message || 'Failed to load sessions. Is the backend running on http://localhost:8080?';
        setError(message);
        console.error('Load sessions error:', err);
      } finally {
        setLoading(false);
      }
    };
    load();
  }, []);

  const handleCreate = async (event: FormEvent) => {
    event.preventDefault();
    if (!name.trim()) {
      setError('Please enter a session name');
      return;
    }
    try {
      setCreating(true);
      setError(null);
      const session = await SessionApi.createSession({ name: name.trim(), description });
      setSessions([session, ...sessions]);
      setName('');
      setDescription('');
    } catch (err: any) {
      const message = err?.response?.data?.message || err?.message || 'Failed to create session. Is the backend running?';
      setError(message);
      console.error('Create session error:', err);
    } finally {
      setCreating(false);
    }
  };

  return (
    <div className="page">
      <header className="page__header">
        <div>
          <p className="eyebrow">Annotation Platform</p>
          <h1>ReID Sessions</h1>
          <p className="subtitle">Upload paired videos, review bounding boxes, and assign cross-camera identities.</p>
        </div>
      </header>

      <section className="card">
        <h2>Create a new session</h2>
        <form className="form" onSubmit={handleCreate}>
          <label>
            <span>Name</span>
            <input value={name} onChange={(event) => setName(event.target.value)} placeholder="E.g. Entrance/Exit Pair 01" required />
          </label>
          <label>
            <span>Description</span>
            <textarea value={description} onChange={(event) => setDescription(event.target.value)} placeholder="Optional notes..." rows={3} />
          </label>
          <button type="submit" disabled={creating}>Create session</button>
        </form>
      </section>

      <section>
        <div className="section-heading">
          <h2>Existing sessions</h2>
          {loading && <span>Loading…</span>}
          {error && <span className="error">{error}</span>}
        </div>
        <div className="grid">
          {sessions.map((session) => (
            <article key={session.id} className="session-card" onClick={() => navigate(`/sessions/${session.id}`)}>
              <h3>{session.name}</h3>
              {session.description && <p>{session.description}</p>}
              <p className="muted">Created {new Date(session.createdAt).toLocaleString()}</p>
              <div className="session-stats">
                <span>Videos: {session.videos.length}</span>
                <span>Identities: {session.identities.length}</span>
              </div>
            </article>
          ))}
          {!loading && sessions.length === 0 && <p>No sessions yet. Create one above to get started.</p>}
        </div>
      </section>
    </div>
  );
};

export default SessionListPage;
