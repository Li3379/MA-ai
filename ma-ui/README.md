# ma-ui 前端项目

基于 Vue 3 + Vite 的医疗 AI 助手用户侧前端。

## 快速开始

```bash
npm install
npm run dev
```

默认开发服务运行在 `http://localhost:5173`。

## 环境变量

复制 `.env.example` 为 `.env.local`，并填写实际的后端接口与 Supabase 配置信息。

```env
VITE_API_BASE_URL=http://localhost:8080
VITE_SUPABASE_URL=https://your-project.supabase.co
VITE_SUPABASE_ANON_KEY=your-anon-key
```

## 项目结构

- `src/router`：路由配置
- `src/stores`：Pinia 状态管理
- `src/views`：页面组件
- `src/api`：后端接口与 Supabase 客户端
- `src/styles`：全局样式

## 后续计划

- 接入真实的健康咨询、运动方案、社区数据接口
- 集成 Supabase 实时聊天与文件存储
- 引入国际化、多主题等增强体验的能力
