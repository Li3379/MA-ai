# 项目初始化说明

## 后端（基于 RuoYi-AI）

- 已引入 `ruoyi-ai` 源码作为后端基础工程。
- 建议在本地运行 `mvn clean install` 验证依赖完整性。
- 可在 `ruoyi-ai` 目录下使用 IDEA 导入 Maven 多模块项目。
- 后续将逐步拆分/重命名模块至规划中的 `ma-admin`、`ma-ai` 等子模块。

### 推荐操作

1. 复制 `ruoyi-ai/script/deploy/mysql-init/ruoyi-ai.sql` 到本地 MySQL 进行初始化。
2. 根据 `ruoyi-ai/README.md` 完成基础配置（如 Redis、MinIO、Weaviate）。
3. 在 `ruoyi-ai` 工程中准备扩展包以集成 langchain4j 与 Supabase。

## 前端（ma-ui）

- 新建 `ma-ui` Vue3 + Vite 工程作为用户体验前端。
- 包含健康咨询、运动方案、社区互助等页面骨架。
- 已预置 Element Plus、Pinia、Vue Router、Supabase SDK 等依赖。
- 后续步骤：
  - 编写 API 对接层与实际后端联调。
  - 引入身份认证（JWT + Supabase Auth）。
  - 增加单元测试与 UI 设计优化。

## 环境文件

- 在根目录 `docs/environment-setup.md` 中记录了开发环境安装指南。
- 前端环境变量模板位于 `ma-ui/.env.example`。

## 下一步

- 完成后端模块化改造与基础接口定义。
- 设计数据库脚本并放入 `sql/` 目录。
- 编写 CI/CD 流程，准备 EdgeOne 部署所需配置。
