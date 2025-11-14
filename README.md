# ReID Annotation Platform

A full-stack application for annotating cross-camera person identities. Annotators upload two synchronized videos that already contain detected bounding boxes, review track summaries, and manually assign global person IDs across both videos. The output dataset can be exported as JSON for ReID model training/testing.

## Tech stack

- **Frontend**: React 18 + Vite + TypeScript
- **Backend**: Spring Boot 2.7 (Java 11)
- **Storage**: Local filesystem (videos + JSON), persisted session metadata stored as JSON under `backend/storage/data/sessions.json`

## Project structure

```
annotation_platform/
├── backend/                 # Spring Boot service (port 8080)
├── frontend/                # React SPA (dev server port 5173)
└── sample-data/             # Example bounding-box JSON files
```

## Prerequisites

- Java 11+
- Maven 3.9+ (needed to build/run the backend)  
  > This environment does not ship with Maven, so the backend build was not executed here. Run `mvn spring-boot:run` locally once Maven is installed.
- Node.js 18+ and npm 9+

## Running the backend

```bash
cd /Users/tianhaozhang/Github/annotation_platform/backend
mvn spring-boot:run
```

Configuration defaults:

- Port: `8080`
- Storage root: `backend/storage`
- Uploaded videos served from `http://localhost:8080/media/...`

## Running the frontend

```bash
cd /Users/tianhaozhang/Github/annotation_platform/frontend
npm install        # already run once, package-lock.json checked in
npm run dev        # starts Vite dev server on http://localhost:5173
```

For production builds:

```bash
npm run build
npm run preview    # serves the dist/ bundle locally
```

Set `VITE_API_BASE_URL` to point to the backend if it is hosted on a different origin.

## Deploying to Render (backend) + Vercel (frontend)

The repo already contains everything you need for a “one click” deployment:

### Backend (Render.com)

1. Fork this repo and connect it to Render.
2. Render auto-detects `render.yaml` at the repo root. It defines a single **Web Service** that:
   - Builds the backend via `backend/Dockerfile` (multi-stage Maven + Java 11 + ffmpeg).
   - Mounts a 10 GB persistent disk at `/data` so uploaded videos and `sessions.json` survive restarts.
   - Sets `SPRING_PROFILES_ACTIVE=prod`, `APP_STORAGE_ROOT=/data/annotation`, and `APP_MEDIA_PREVIEW_FFMPEG=/usr/bin/ffmpeg`.
3. Click “New +” → “Blueprint” in Render, select this repo, and deploy. The resulting URL (e.g. `https://annotation-backend.onrender.com`) exposes both the API and `/media/...` streaming endpoints.

> `backend/src/main/resources/application-prod.properties` reads the environment variables above and automatically binds to Render’s `$PORT`.

### Frontend (Vercel)

1. Import the same GitHub repo into Vercel.
2. In the project settings set **Root Directory** = `frontend/`. (Alternatively, keep the repo root and leave `vercel.json` at the top-level; it already points Vercel to `frontend/package.json`.)
3. Define the environment variable `VITE_API_BASE_URL` in both Preview and Production environments. Set its value to the Render backend URL from the previous step.
4. Deploy. Vercel will run `npm install && npm run build` inside `frontend/`, publish `frontend/dist/`, and route all SPA requests back to `index.html` (thanks to `vercel.json`).

The end result is:

- `https://<vercel-app>.vercel.app` – React UI
- `https://<render-service>.onrender.com` – Spring Boot API + media streaming

Uploads from the Vercel app go directly to Render, where ffmpeg generates browser-friendly previews before the UI reloads.

## Bounding box JSON format

Upload a JSON array (one file per video) with the following schema:

```jsonc
[
  {
    "boxId": "a_f001_t1",      // unique ID per box (auto-generated if omitted)
    "frameIndex": 1,             // integer frame number
    "x": 410,
    "y": 220,
    "width": 140,
    "height": 360,
    "frameWidth": 1280,          // used for overlay scaling (optional but recommended)
    "frameHeight": 720,
    "trackId": "entrance_track_1" // identifier used when matching identities
  }
]
```

Sample files live under `sample-data/boxes-a.json` and `sample-data/boxes-b.json`.

## Typical workflow

1. **Create session** – From the landing page, enter a session name/description.
2. **Upload videos + boxes** – For slots A and B, upload the MP4 (or any video format) and the matching bounding-box JSON file.
3. **Inspect tracks** – Each video’s bounding boxes are summarized into tracks (grouped by `trackId`).
4. **Match identities** – Select tracks from both videos and either:
   - Enter a new label and click “Create identity”, or
   - Click an existing identity chip to append the selected tracks to it.
5. **Export dataset** – Use the “Export JSON” button to download the entire session (videos metadata, boxes, identities).

## API highlights

| Method | Endpoint | Description |
| --- | --- | --- |
| `POST` | `/api/sessions` | Create a session |
| `GET` | `/api/sessions` | List sessions |
| `GET` | `/api/sessions/{id}` | Session details |
| `POST` | `/api/sessions/{id}/videos/{slot}` | Upload video + boxes (`slot` = `a` or `b`) |
| `POST` | `/api/sessions/{id}/identities` | Create identity |
| `PUT` | `/api/sessions/{id}/identities/{identityId}` | Replace label/occurrences |
| `DELETE` | `/api/sessions/{id}/identities/{identityId}` | Remove identity |
| `GET` | `/api/sessions/{id}/export` | Download entire session as JSON |

All responses are JSON; file uploads are handled with multipart form-data.