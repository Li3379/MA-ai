# Supabase 集成方案

## 1. Authentication

### 前端

- 使用 `@supabase/supabase-js` 初始化客户端（见 `ma-ui/src/api/supabase.ts`）。
- 登录注册流程：
  1. 调用 `supabaseClient.auth.signInWithPassword({ email, password })`。
  2. 成功后获得 `session`，其中包含 `access_token`。
  3. 将 `access_token` 透传给后端（放入 `Authorization: Bearer` 头）。
- 社交登录：
  - 调用 `signInWithOAuth({ provider: 'wechat' })` 等（需在 Supabase Dashboard 启用对应 Provider）。

### 后端

- 在 Spring Boot 中校验 Supabase JWT：
  - 通过 `https://<project>.supabase.co/auth/v1/jwks` 获取 JWKS。
  - 使用 `Nimbus JOSE JWT` 或 `jjwt` 来解析并验证。
  - 验证成功后，将 `sub` 映射为本地用户。
- 可扩展 `SupabaseAuthFilter`，在 RuoYi 安全集成前执行 JWT 校验。

## 2. Storage

- 在 Supabase 创建 `medical-files`、`community` 等存储桶。
- 设置访问策略：
  - 头像、社区图片使用公共读权限。
  - 健康报告、处方单需设置为受保护，需签名访问。
- 前端上传流程：
  - 调用 `supabaseClient.storage.from('medical-files').upload(path, file)`。
  - 返回 `publicUrl` 或使用 `createSignedUrl`。

## 3. Realtime

- 在 `ma-ui/src/views/CommunityView.vue` 示例中，通过 `supabase.channel` 订阅消息。
- 服务端推送：
  - 启用 `database` Webhook，同步 `chat_messages` 表变更。
  - 对于需要服务器发布的通知，可调用 `supabaseClient.channel` REST API。

## 4. 环境变量

在 `ma-ui/.env.example` 中已列出：

```
VITE_SUPABASE_URL=https://your-project.supabase.co
VITE_SUPABASE_ANON_KEY=your-anon-key
```

建议在后端 `application-*.yml` 中增加：

```yaml
supabase:
  api-url: ${SUPABASE_API_URL:}
  anon-key: ${SUPABASE_ANON_KEY:}
  service-key: ${SUPABASE_SERVICE_KEY:}
  storage:
    bucket-medical: medical-files
    bucket-community: community
```

## 5. 后续计划

1. 实现 `SupabaseService`，封装 JWT 验证、文件签名 URL 生成功能。
2. 使用 Supabase Edge Functions 编写内容审核/通知逻辑。
3. 与 AI 模块联动，将聊天记录写入 `chat_messages` 表，实现前端实时更新。
