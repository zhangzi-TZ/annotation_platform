# Surge.sh 部署指南

## 步骤

1. **安装 Surge**
   ```bash
   npm install -g surge
   ```

2. **构建前端**
   ```bash
   cd frontend
   npm run build
   ```

3. **部署**
   ```bash
   cd dist
   surge
   ```
   - 首次使用需要注册（免费）
   - 输入域名（例如：`annotation-platform.surge.sh`）

4. **配置环境变量**
   - 在构建前设置：
   ```bash
   export VITE_API_BASE_URL=https://annotation-platform.onrender.com
   npm run build
   ```

## 优势

- ✅ 完全免费
- ✅ 部署快速（命令行）
- ✅ 自定义域名支持

