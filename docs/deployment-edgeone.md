# EdgeOne 部署方案

## 架构概览

1. **应用服务器**：
   - 腾讯云 CVM / 轻量应用服务器
   - Docker Compose 部署后端（ruoyi-ai）、前端（ma-ui 静态资源）
   - MySQL、Redis、PGVector 本地或托管服务

2. **EdgeOne**：
   - 作为全球加速与安全防护入口
   - 提供 WAF、DDoS 防护、Bot 管理
   - 支持静态资源缓存与智能路由

## 部署步骤

1. **容器化**
   - 后端：`mvn clean package -DskipTests` -> 构建 Docker 镜像
   - 前端：`npm install && npm run build` -> 使用 Nginx 镜像托管 `dist`
   - 参考 `ruoyi-ai/script/deploy` 中的 Dockerfile 和 Compose 模板

2. **服务启动**
   - 编辑 `docker-compose.yml`，包含：
     - `ma-backend`（Spring Boot）
     - `ma-frontend`（Nginx 静态站点）
     - `mysql`、`redis`
   - `docker compose up -d`

3. **EdgeOne 接入**
   - 在 EdgeOne 控制台创建站点，CNAME 指向平台提供的接入域名
   - 配置节点回源：
     - 静态资源指向 `ma-frontend`（Nginx 80）
     - API 指向 `ma-backend`（Spring Boot 8080）
   - 配置缓存策略：
     - `/assets/*` 设置长缓存
     - `/api/*` 设置不缓存

4. **HTTPS 与证书**
   - 在 EdgeOne 申请免费证书或上传已有证书
   - 启用 HSTS、HTTP/2，增强安全

5. **安全策略**
   - 开启 WAF 规则，防护 SQL 注入、XSS
   - 配置访问控制：限制敏感接口访问频率
   - 启用 Bot 防护，对抗恶意爬虫

6. **监控告警**
   - EdgeOne 带宽、请求量、攻击日志监控
   - 服务器侧使用 Prometheus + Grafana 或云监控
   - 设置日志采集，将应用日志集中到 Elasticsearch 或云日志服务

## CI/CD 建议

- 使用 GitHub Actions 或腾讯云 CODING 部署流水线
- 自动构建镜像，推送至容器镜像服务（TCR）
- 部署阶段触发 `docker compose pull && docker compose up -d`

## 备份与恢复

- MySQL：开启自动备份，定期导出
- Supabase：关注存储与数据库使用量
- 配置对象存储桶备份用户上传文件
