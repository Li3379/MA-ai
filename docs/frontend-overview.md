# 前端整体结构

## 主要页面

- `HomeView`：展示核心功能入口
- `ConsultView`：AI 健康咨询聊天界面，调用 `/api/ai/consult/chat`
- `ExerciseView`：健康档案 + 运动方案生成
- `CommunityView`：Supabase 社区帖子展示与发帖
- `AuthView`：登录/注册（结合 Supabase Auth + 后端认证）

## 状态与路由

- `Pinia` 用于后续全局状态管理（目前已初始化）
- `Vue Router` 管理 `/home`、`/consult`、`/exercise`、`/community`、`/login`
- `AppLayout` 统一头部导航、底部版权

## 接口封装

- `src/api/http.ts`：Axios 封装，自动附带本地 Token
- `src/api/auth.ts`：Supabase + 后端混合登录
- `src/api/consult.ts`：AI 咨询接口
- `src/api/exercise.ts`：运动方案生成
- `src/api/community.ts`：Supabase 社区操作

## 样式

- 使用 Element Plus 组件库 + 全局 `global.scss`
- Layout 自适应，补充骨架屏、空状态提示

## 待办

- 引入全局状态存储用户信息
- 优化移动端适配
- 增加错误边界与 Loading 反馈
- 引入国际化与深浅色主题
