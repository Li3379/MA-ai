# 测试与优化计划

## 单元测试

- 后端：使用 JUnit5 + Spring Boot Test，覆盖 AI 接口、Supabase 服务的边界条件
- 前端：使用 Vitest + Vue Testing Library，验证组件渲染与交互

## 集成测试

- 模拟登录流程：Supabase Token -> 后端 `/auth/login`
- 健康咨询：调用 `/api/ai/consult/chat`，验证模型响应
- 运动方案：调用 `/api/ai/exercise/plan`，校验 JSON 格式
- 社区发帖：Supabase CRUD + RLS 权限测试

## 性能优化

- 启用 Langchain4j 流式输出，减少响应时间
- 为 MySQL 表新增索引（`health_consultation.user_id` 等）
- 使用 Redis 缓存热门帖子、常用提示词

## 安全与合规

- XSS/SQL 注入检测
- API 频率限制、敏感词过滤
- 日志脱敏、权限审计

## 工具链

- 使用 GitHub Actions / Jenkins 自动化构建
- SonarQube 代码质量扫描
- Docker Compose 本地联调（MySQL、Redis、PGVector、Supabase Edge Function Mock）
