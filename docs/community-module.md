# 社区互助模块

## 功能点

- 列表展示 `community_posts` 表的最新帖子（Supabase 查询）
- 支持发帖弹窗，直接写入 Supabase PostgreSQL
- 预留实时聊天入口，后续集成 Realtime 消息
- 前端提供点赞/评论统计占位信息

## 关键文件

- `ma-ui/src/api/community.ts`：封装列表、发帖、聊天室订阅方法
- `CommunityView.vue`：页面逻辑，使用 Skeleton + Space 渲染帖子
- `sql/supabase/community_schema.sql`：数据库建表与 RLS 策略

## 后续任务

1. 完善评论、点赞等互动接口。
2. 引入内容审核（敏感词过滤、AI 审核）。
3. 使用 Supabase Storage 上传社区图片。
4. 将 Realtime 聊天与后端 AI 模块打通，实现专家介入。
