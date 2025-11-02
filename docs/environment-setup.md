# 开发环境搭建指南

## 必备软件

1. **JDK 17**
   - 下载链接：https://www.oracle.com/java/technologies/downloads/
   - 安装后设置 `JAVA_HOME`，并将 `%JAVA_HOME%\bin` 加入 `PATH`

2. **Apache Maven 3.8+**
   - 下载链接：https://maven.apache.org/download.cgi
   - 解压后设置 `MAVEN_HOME`，并将 `%MAVEN_HOME%\bin` 加入 `PATH`

3. **Node.js 18+**
   - 下载链接：https://nodejs.org/zh-cn/download
   - 安装后确认 `node` 与 `npm` 版本满足要求

4. **MySQL 8.0**
   - 下载链接：https://dev.mysql.com/downloads/mysql/
   - 安装后配置 root 密码，字符集建议使用 `utf8mb4`

5. **Redis 7.x**
   - Windows 开发环境可使用 https://github.com/microsoftarchive/redis/releases
   - 生产环境建议部署在 Linux 服务器

6. **Git**
   - 下载链接：https://git-scm.com/downloads
   - 配置全局用户名与邮箱

## 云服务账号准备

1. **阿里云百炼（通义千问）**
   - 注册并实名认证阿里云账号：https://www.aliyun.com/
   - 进入百炼控制台申请 API Key：https://bailian.console.aliyun.com
   - 记录模型名称（如 `qwen-plus`）与请求调用额度

2. **Supabase**
   - 注册 Supabase 账号：https://supabase.com/
   - 创建新项目，选择最近的区域
   - 在项目设置中获取 `Project URL` 与 `anon`/`service_role` API key
   - 启用 Authentication、Storage、Realtime 服务

## 开发工具推荐

- **IDEA Ultimate 2024+**：用于后端 Java 开发
- **VS Code / WebStorm**：用于前端 Vue3 开发
- **Docker Desktop**：可用于本地快速启动 MySQL、Redis
- **Postman / Apifox**：调试后端 API

## 验证安装

安装完成后，可在命令行中运行以下命令验证：

```bash
java -version
mvn -version
node -v
npm -v
mysql --version
redis-cli --version
git --version
```

若命令输出版本号且满足要求，说明环境搭建成功。
