# ReID Annotation Platform

A web-based annotation tool for person re-identification across multiple videos. Upload two videos with pre-existing bounding boxes, assign consistent identity IDs across both videos, and export a ReID-ready labeled dataset.

## 🌐 Live Demo

- **Frontend**: [https://annotation-platform.pages.dev](https://annotation-platform.pages.dev)
- **Backend API**: [https://annotation-platform.onrender.com](https://annotation-platform.onrender.com)

## 🚀 Quick Start

### Prerequisites

- Java 11+
- Maven 3.9+
- Node.js 18+ and npm 9+

### Local Development

**Backend:**
```bash
cd backend
mvn spring-boot:run
# Runs on http://localhost:8080
```

**Frontend:**
```bash
cd frontend
npm install
npm run dev
# Runs on http://localhost:5173
```

## 📋 Workflow

1. **Create Session** - Enter a session name and description
2. **Upload Videos** - Upload two videos (Video A and Video B) with their corresponding bounding box JSON files
3. **Review Tracks** - Each video's bounding boxes are grouped by `trackId` for easy review
4. **Match Identities** - Select tracks from both videos and assign them to the same person identity
5. **Export Dataset** - Download the annotated session as JSON for ReID model training

## 🎬 Sample Data

The repository includes sample test videos and bounding box JSON files generated using the script in `sample-data/generate_test_video.py`. You can download them directly from the repository:

- `sample-data/test_video_a.mp4` - Sample video for slot A
- `sample-data/test_video_b.mp4` - Sample video for slot B
- `sample-data/test_boxes_a.json` - Bounding boxes for video A
- `sample-data/test_boxes_b.json` - Bounding boxes for video B

To generate your own test videos, run:
```bash
cd sample-data
python generate_test_video.py
```

## 📦 Bounding Box JSON Format

```json
[
  {
    "boxId": "a_f001_t1",
    "frameIndex": 1,
    "x": 410,
    "y": 220,
    "width": 140,
    "height": 360,
    "frameWidth": 1280,
    "frameHeight": 720,
    "trackId": "entrance_track_1"
  }
]
```

## 🛠️ Tech Stack

- **Frontend**: React 18 + Vite + TypeScript
- **Backend**: Spring Boot 2.7 (Java 11)
- **Storage**: Local filesystem (videos + JSON)

## 📚 API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/sessions` | Create a session |
| `GET` | `/api/sessions` | List sessions |
| `GET` | `/api/sessions/{id}` | Session details |
| `POST` | `/api/sessions/{id}/videos/{slot}` | Upload video + boxes |
| `POST` | `/api/sessions/{id}/identities` | Create identity |
| `PUT` | `/api/sessions/{id}/identities/{identityId}` | Update identity |
| `DELETE` | `/api/sessions/{id}/identities/{identityId}` | Delete identity |
| `GET` | `/api/sessions/{id}/export` | Export session as JSON |

## 🚢 Deployment

The application is deployed using:
- **Backend**: [Render](https://render.com) (Spring Boot service)
- **Frontend**: [Cloudflare Pages](https://pages.cloudflare.com) (React SPA)

See deployment configuration files:
- `render.yaml` - Render backend configuration
- `netlify.toml` - Alternative frontend deployment (Netlify)
- `backend/Dockerfile` - Backend container image

## 📄 License

This project is open source and available for educational and research purposes.
