# 数据库设计概要

## MySQL 核心表

| 表名 | 用途 | 关键字段 |
| --- | --- | --- |
| `health_profile` | 存储用户健康档案 | `user_id`、`chronic_diseases`、`lifestyle` |
| `health_consultation` | 记录 AI 健康咨询会话 | `consultation_no`、`status`、`risk_level` |
| `consultation_message` | 保存咨询中的消息往来 | `sender_role`、`message_type` |
| `ai_inference_log` | 追踪模型调用情况 | `model`、`total_tokens`、`response_status` |
| `exercise_plan` | AI 生成运动方案 | `plan_no`、`goal`、`duration_weeks` |
| `exercise_plan_schedule` | 方案日程明细 | `day_index`、`activities` |
| `exercise_record` | 用户执行反馈 | `status`、`calorie_burn` |
| `ai_config` | AI 服务配置中心 | `provider`、`model`、`temperature` |
| `ai_prompt_template` | 提示词模板管理 | `template_code`、`template_type` |

所有表均与 RuoYi 原有的 `sys_user` 通过 `user_id` 关联，确保权限体系统一。

## Supabase PostgreSQL 表

| 表名 | 用途 |
| --- | --- |
| `community_posts` | 社区帖子，支持分类、标签与统计字段 |
| `community_comments` | 帖子评论与回复，支持多级嵌套 |
| `community_post_reactions` | 点赞/表态记录 |
| `chat_rooms` | 实时聊天房间（群聊/私聊） |
| `chat_room_members` | 房间成员关系 |
| `chat_messages` | 实时聊天消息内容 |
| `user_files` | Supabase Storage 对应的文件元数据 |

已附带基础 RLS 策略，限制非成员访问敏感数据。

## 后续任务

- 将 MySQL 表结构脚本导入并生成对应的 MyBatis Plus 实体类。
- 在 Supabase 中创建 Bucket（如 `community`、`medical-files`）并配置访问策略。
- 对接数据同步任务（例如将聊天消息摘要同步到 ElasticSearch 以便检索）。
