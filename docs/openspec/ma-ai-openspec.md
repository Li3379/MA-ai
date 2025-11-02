# ma-ai 医疗AI助手 OpenSpec 提案

```yaml
OpenSpec:
  version: 1.0
  title: ma-ai 医疗AI助手
  description: >
    面向中小型医疗健康场景的 AI 助手平台，提供健康咨询、运动方案、社区互助，
    并集成阿里云百炼通义千问与 Supabase 服务。
  owners:
    product_owner: "贵方团队"
    tech_owner: "贵方技术负责人"
  stakeholders:
    - 产品经理
    - 医疗合规顾问
    - 前端/后端工程师
    - 运维工程师
  constraints:
    - 必须满足中国大陆医疗健康信息合规要求
    - 使用已有系统变量管理密钥（DASH_SCOPE_API_KEY、VITE_SUPABASE_URL、VITE_SUPABASE_ANON_KEY）
    - 前端采用 Vue3 生态，后端基于 RuoYi-AI 多模块架构
  milestones:
    - 环境与基础设施准备
    - 开发与联调
    - 测试与合规验证
    - EdgeOne 部署与上线
  success_metrics:
    - 95% 接口用例通过率
    - AI 响应平均延时 < 2s
    - 首月 NPS ≥ 8
    - EdgeOne WAF 告警为 0 且无重大安全事件
```

---

## 项目开发至上线流程

1. **环境与基础设施准备**
   - 拉取仓库、执行 `sql/mysql/ma_ai_schema.sql` 与 Supabase 表脚本。
   - 确认服务器/本地已配置系统变量：`DASH_SCOPE_API_KEY`、`VITE_SUPABASE_URL`、`VITE_SUPABASE_ANON_KEY`。
   - 前端 `.env.local` 引用 `VITE_SUPABASE_*`，后端 `application-*.yml` 中引用 `DASH_SCOPE_API_KEY` 与 `qwen3-max` 模型名。

2. **核心功能联调**
   - 健康咨询接口 `POST /api/ai/consult/chat` 调通义千问（流式可选）。
   - 运动方案接口 `POST /api/ai/exercise/plan` 输出结构化 JSON。
   - 社区模块通过 Supabase-js 读写 `community_posts` 并验证 RLS。
   - 用户登录流程：Supabase Auth -> `/auth/login` 映射本地用户。

3. **测试与合规**
   - 后端 `mvn test`、前端 `npm run test`。
   - 性能优化：Redis 缓存、AI Streaming、SQL 索引。
   - 安全：WAF、敏感词过滤、日志脱敏、医疗免责声明。

4. **EdgeOne 部署上线**
   - Docker 化后端、前端；`docker compose up -d`。
   - EdgeOne 配置 CNAME、证书、WAF、缓存策略（静态资源缓存、API 不缓存）。
   - 监控：EdgeOne 图表 + 服务器 Prometheus/Grafana 或云监控。
   - 灰度发布、全量上线、持续监控。

5. **持续运营与优化**
   - 统计 AI 调用与咨询数据，收集用户反馈。
   - 扩展知识库管理后台、Supabase 实时聊天、内容审核流程。
   - 建立 CI/CD 流程（GitHub Actions/CODING）自动构建与部署。
