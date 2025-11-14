# Cloudflare Pages 部署指南

## 步骤

1. **登录 Cloudflare Pages**
   - 访问：https://pages.cloudflare.com
   - 使用 Cloudflare 账户登录（如果没有，免费注册）

2. **连接 GitHub 仓库**
   - 点击 "Create a project"
   - 选择 "Connect to Git"
   - 授权并选择仓库 `zhangzi-TZ/annotation_platform`

3. **配置构建设置**
   - **Framework preset**: `Vite`
   - **Build command**: `npm run build`
   - **Build output directory**: `frontend/dist`
   - **Root directory**: `frontend`

4. **添加环境变量**
   - 在构建设置中，找到 "Environment variables"
   - 添加：
     - **Variable name**: `VITE_API_BASE_URL`
     - **Value**: `https://annotation-platform.onrender.com`

5. **部署**
   - 点击 "Save and Deploy"
   - 等待构建完成（约 1-2 分钟）

## 优势

- ✅ 完全免费
- ✅ 全球 CDN，速度快
- ✅ 自动 HTTPS
- ✅ 环境变量配置简单
- ✅ 支持预览部署（PR 自动部署）

