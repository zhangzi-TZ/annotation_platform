# 测试视频获取指南

## 快速开始

这个目录包含示例 bounding box JSON 文件，但你需要自己准备测试视频。

## 选项 1: 使用公开数据集（推荐）

### MOT Challenge 数据集
- **网站**: https://motchallenge.net/
- **说明**: 包含多目标跟踪视频，适合 ReID 标注
- **下载**: 选择 "MOT17" 或 "MOT20" 数据集，下载任意两个不同摄像头的视频片段

### DukeMTMC 数据集（学术用途）
- **网站**: https://github.com/layumi/DukeMTMC-reID_evaluation
- **说明**: 专门用于 ReID 研究，包含多个摄像头视角
- **注意**: 需要申请访问权限

### Market-1501 数据集
- **网站**: https://zheng-lab.cecs.anu.edu.au/Project/project_reid.html
- **说明**: 包含 6 个摄像头视角的行人视频

## 选项 2: 自己录制

如果你有摄像头，可以：
1. 录制两个不同角度的视频（例如：入口和出口）
2. 确保视频中包含相同的人群
3. 使用检测工具（如 YOLO、Detectron2）生成 bounding boxes

## 选项 3: 使用示例视频生成脚本

你可以使用 Python 脚本生成简单的测试视频：

```python
# generate_test_video.py
import cv2
import numpy as np

def create_test_video(filename, num_frames=30, width=1280, height=720):
    fourcc = cv2.VideoWriter_fourcc(*'mp4v')
    out = cv2.VideoWriter(filename, fourcc, 10.0, (width, height))
    
    for i in range(num_frames):
        # 创建简单的背景
        frame = np.ones((height, width, 3), dtype=np.uint8) * 200
        
        # 添加一些"行人"矩形（模拟 bounding boxes）
        x = 400 + int(10 * np.sin(i * 0.1))
        y = 200 + int(5 * np.cos(i * 0.1))
        cv2.rectangle(frame, (x, y), (x+100, y+200), (0, 255, 0), 2)
        cv2.putText(frame, f'Frame {i+1}', (10, 30), 
                   cv2.FONT_HERSHEY_SIMPLEX, 1, (255, 255, 255), 2)
        
        out.write(frame)
    
    out.release()
    print(f"Created {filename}")

# 生成两个测试视频
create_test_video('test_video_a.mp4', num_frames=30)
create_test_video('test_video_b.mp4', num_frames=30)
```

## 选项 4: 使用在线视频资源

### YouTube 视频（需要下载）
- 搜索 "surveillance camera" 或 "CCTV footage"
- 使用工具如 `yt-dlp` 下载：
  ```bash
  pip install yt-dlp
  yt-dlp -f "bestvideo[ext=mp4]" <video_url> -o test_video.mp4
  ```
- **注意**: 确保遵守版权和使用条款

## 推荐的测试流程

1. **准备两个视频**：
   - `video_a.mp4` - 第一个摄像头视角
   - `video_b.mp4` - 第二个摄像头视角（包含相同的人群）

2. **生成或准备 bounding boxes JSON**：
   - 如果使用检测模型，导出为 JSON 格式
   - 或者手动创建（参考 `boxes-a.json` 和 `boxes-b.json`）

3. **上传到平台**：
   - 创建新会话
   - 上传 Video A + boxes-a.json
   - 上传 Video B + boxes-b.json
   - 开始标注！

## 最小测试示例

即使没有真实视频，你也可以：
1. 使用任意两个短视频（甚至可以是同一个视频上传两次）
2. 使用 `sample-data/boxes-a.json` 和 `boxes-b.json` 作为 bounding boxes
3. 测试平台的完整工作流程

## 注意事项

- 视频格式：支持 MP4, MOV, AVI, WebM 等常见格式
- 视频大小：建议每个视频 < 100MB（可在 `application.properties` 中调整）
- Bounding boxes：确保 JSON 中的 `frameIndex` 与视频帧数匹配
- 帧率：平台不依赖帧率，但建议使用 10-30 FPS 的视频

