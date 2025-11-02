# 后台管理模块规划

## 目标

- 复用 RuoYi 原生系统管理能力（用户、角色、菜单、部门、日志）
- 新增 AI 配置、内容审核、数据看板等医疗场景功能

## 建议菜单结构

1. **系统管理**（沿用原有）
2. **AI 管理**
   - 模型配置：维护 `ai_config`、`ai_prompt_template`
   - 调用日志：查看 `ai_inference_log`
3. **医疗咨询**
   - 会话记录管理：`health_consultation`
   - 消息记录：`consultation_message`
4. **运动方案**
   - 方案审核、用户反馈
5. **社区管理**
   - 帖子审核：联通 Supabase 管理接口
   - 举报处理

## 实施步骤

1. 在 `ruoyi-admin` 中新增菜单路由，指向 Vue Admin 前端（待集成）。
2. 编写 MyBatis Mapper & Service 访问医疗业务表。
3. 利用 RuoYi 的 `@RepeatSubmit`、`@Log` 注解增强审计能力。
4. 集成 Supabase Service，调用其 REST Admin API 做帖子审核、封禁等操作。
5. 编写数据大屏：汇总咨询数量、AI 调用次数、用户活跃度。

## 下一步

- 设计后台前端页面（可复用 RuoYi-Vue 前端或重构为 Vue3 版本）。
- 编写内容审核工作流（AI + 人工审批）。
- 增加 EdgeOne 接入后的安全统计页面。
