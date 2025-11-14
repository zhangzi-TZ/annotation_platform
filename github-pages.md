# GitHub Pages 部署指南

## 步骤

1. **安装 gh-pages 包**
   ```bash
   cd frontend
   npm install --save-dev gh-pages
   ```

2. **更新 package.json**
   在 `scripts` 中添加：
   ```json
   "deploy": "npm run build && gh-pages -d dist"
   ```

3. **配置 vite.config.ts**
   添加 `base` 配置：
   ```typescript
   export default defineConfig({
     base: '/annotation_platform/',
     // ... 其他配置
   });
   ```

4. **部署**
   ```bash
   npm run deploy
   ```

5. **启用 GitHub Pages**
   - 在 GitHub 仓库设置中
   - Settings → Pages
   - Source: `gh-pages` branch
   - 访问：`https://zhangzi-TZ.github.io/annotation_platform/`

## 注意

- 需要配置环境变量（通过构建时注入）
- 免费但功能较基础

