#!/usr/bin/env python3
"""
生成测试视频和匹配的 bounding boxes JSON
需要安装: pip install opencv-python numpy
"""

import cv2
import numpy as np
import json
import sys
import os

def create_test_video_with_boxes(filename, boxes_filename, num_frames=30, width=1280, height=720, 
                                 person_x=400, person_y=200, track_id="track_1", slot="A"):
    """创建测试视频并生成匹配的 bounding boxes JSON"""
    fourcc = cv2.VideoWriter_fourcc(*'mp4v')
    out = cv2.VideoWriter(filename, fourcc, 10.0, (width, height))
    
    if not out.isOpened():
        print(f"Error: Could not open {filename}")
        return False
    
    print(f"Generating {filename} ({num_frames} frames)...")
    
    boxes = []
    box_width = 120
    box_height = 200
    
    # 整个视频使用同一个 track
    current_track_id = f"{slot.lower()}_track_1"
    
    for i in range(num_frames):
        # 创建灰色背景
        frame = np.ones((height, width, 3), dtype=np.uint8) * 200
        
        # 模拟行人移动（简单的正弦波运动，平滑移动）
        x = person_x + int(30 * np.sin(i * 0.1))
        y = person_y + int(15 * np.cos(i * 0.08))
        
        # 确保坐标在范围内
        x = max(50, min(width - box_width - 50, x))
        y = max(50, min(height - box_height - 50, y))
        
        # 绘制"行人"矩形（绿色边框）
        cv2.rectangle(frame, (x, y), (x+box_width, y+box_height), (0, 255, 0), 3)
        cv2.rectangle(frame, (x+10, y+10), (x+box_width-10, y+box_height-10), (100, 200, 100), -1)
        
        # 添加帧号和 track 标签
        cv2.putText(frame, f'Frame {i+1}', (10, 30), 
                   cv2.FONT_HERSHEY_SIMPLEX, 1, (255, 255, 255), 2)
        cv2.putText(frame, f'Track: {current_track_id}', (10, 70), 
                   cv2.FONT_HERSHEY_SIMPLEX, 0.7, (200, 200, 200), 2)
        
        out.write(frame)
        
        # 每隔几帧记录一个 bounding box（模拟检测结果）
        # 每5帧记录一次，确保有足够的boxes来形成track
        if i % 5 == 0 or i == 0:  # 第 0, 5, 10, 15, 20, 25, 30 帧
            box = {
                "boxId": f"{slot.lower()}_f{i+1:03d}_{current_track_id}",
                "frameIndex": i + 1,  # 1-indexed
                "x": float(x),
                "y": float(y),
                "width": float(box_width),
                "height": float(box_height),
                "frameWidth": float(width),
                "frameHeight": float(height),
                "trackId": current_track_id
            }
            boxes.append(box)
    
    out.release()
    print(f"✓ Created {filename}")
    
    # 保存 bounding boxes JSON
    with open(boxes_filename, 'w') as f:
        json.dump(boxes, f, indent=2)
    print(f"✓ Created {boxes_filename} with {len(boxes)} bounding boxes")
    
    return True

def main():
    output_dir = os.path.dirname(os.path.abspath(__file__))
    
    # 生成 Video A 和匹配的 boxes
    video_a = os.path.join(output_dir, 'test_video_a.mp4')
    boxes_a = os.path.join(output_dir, 'test_boxes_a.json')
    create_test_video_with_boxes(
        video_a, boxes_a, 
        num_frames=30, 
        person_x=400, person_y=200, 
        track_id="a_track", 
        slot="A"
    )
    
    # 生成 Video B 和匹配的 boxes（不同的位置和 tracks）
    video_b = os.path.join(output_dir, 'test_video_b.mp4')
    boxes_b = os.path.join(output_dir, 'test_boxes_b.json')
    create_test_video_with_boxes(
        video_b, boxes_b, 
        num_frames=30, 
        person_x=600, person_y=250, 
        track_id="b_track", 
        slot="B"
    )
    
    print("\n✓ Test videos and bounding boxes generated!")
    print(f"\nVideo A:")
    print(f"  - {video_a}")
    print(f"  - {boxes_a}")
    print(f"\nVideo B:")
    print(f"  - {video_b}")
    print(f"  - {boxes_b}")
    print("\n💡 Usage:")
    print("  1. Create a session in the platform")
    print("  2. Upload test_video_a.mp4 + test_boxes_a.json to slot A")
    print("  3. Upload test_video_b.mp4 + test_boxes_b.json to slot B")
    print("  4. Match tracks from both videos to create identities!")

if __name__ == '__main__':
    try:
        main()
    except ImportError as e:
        print("Error: Missing required library")
        print("Install with: pip install opencv-python numpy")
        sys.exit(1)
    except Exception as e:
        print(f"Error: {e}")
        import traceback
        traceback.print_exc()
        sys.exit(1)
