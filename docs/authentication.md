# 用户认证方案

## 混合认证流程

1. **Supabase** 负责用户注册、邮箱验证、社交登录等流程。
2. **RuoYi** 负责 RBAC 权限控制、后台管理、接口签权。
3. 登录步骤：
   - 前端调用 Supabase `signInWithPassword`，得到 `access_token`。
   - 前端将 Token 写入 `localStorage`，并请求后端 `/auth/login`。
   - 后端使用 `SupabaseAuthService` 校验 Token，返回本地会话信息（后续可签发自有 JWT）。

## 代码结构

- `ma-ui/src/api/auth.ts`：封装登录、注册、登出逻辑。
- `AuthView.vue`：完成基础页面与调用。
- `ruoyi-modules/ruoyi-chat/.../AuthController.java`：提供 REST API。
- `SupabaseAuthService`：校验 Supabase JWT、生成存储签名链接。

## 后续工作

- 将 Supabase 用户同步到 `sys_user`，映射角色（普通用户/医生/管理员）。
- 引入 RuoYi 原生的 JWT 或 Token 机制，统一接口鉴权。
- 落地多因子认证（短信/邮箱验证码）。
- 对接权限中心，控制社区发帖、后台审核等操作。
