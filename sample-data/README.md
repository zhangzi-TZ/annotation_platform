# Sample Data

This directory contains sample test videos and bounding box JSON files for testing the annotation platform.

## Generated Test Videos

The sample videos (`test_video_a.mp4` and `test_video_b.mp4`) and their corresponding bounding box files (`test_boxes_a.json` and `test_boxes_b.json`) are generated using the Python script `generate_test_video.py`.

### How to Generate

1. **Prerequisites:**
   ```bash
   pip install opencv-python numpy
   ```

2. **Run the script:**
   ```bash
   cd sample-data
   python generate_test_video.py
   ```

3. **Output:**
   - `test_video_a.mp4` - Test video for slot A with a moving object
   - `test_video_b.mp4` - Test video for slot B with a moving object
   - `test_boxes_a.json` - Bounding boxes for video A (one track per video)
   - `test_boxes_b.json` - Bounding boxes for video B (one track per video)

### Script Details

The script generates:
- Two MP4 videos (1280x720, 30 frames each) with a simple moving rectangle
- Corresponding JSON files with bounding box coordinates for each frame
- Each video contains a single track (`a_track_1` and `b_track_1`) to simulate a person moving across frames

### Using the Sample Data

1. Download the sample files from the repository
2. Create a new session in the annotation platform
3. Upload `test_video_a.mp4` and `test_boxes_a.json` for Video A
4. Upload `test_video_b.mp4` and `test_boxes_b.json` for Video B
5. Start annotating by matching the tracks from both videos to the same identity

## File Format

The bounding box JSON files follow this structure:

```json
[
  {
    "boxId": "a_f001_a_track_1",
    "frameIndex": 1,
    "x": 400.0,
    "y": 215.0,
    "width": 120.0,
    "height": 200.0,
    "frameWidth": 1280.0,
    "frameHeight": 720.0,
    "trackId": "a_track_1"
  }
]
```
