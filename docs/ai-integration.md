# LangChain4j + 通义千问集成说明

## 依赖配置

在 `ruoyi-ai/ruoyi-modules/ruoyi-chat/pom.xml` 中新增了 `langchain4j` 相关依赖：

- `langchain4j-spring-boot-starter`
- `langchain4j-qwen`
- `langchain4j-embeddings-pgvector`

统一使用 `langchain4j.version=0.34.0`。

## 配置项

在 Spring Boot 配置文件中新增以下参数：

```yaml
langchain:
  qwen:
    api-key: ${QWEN_API_KEY}
    model: qwen-plus
    endpoint: https://dashscope.aliyuncs.com/compatible-mode/v1
    temperature: 0.7
    top-p: 0.95
    max-tokens: 2048
```

可通过环境变量覆盖 PGVector 向量库连接信息：

- `PGVECTOR_HOST`
- `PGVECTOR_PORT`
- `PGVECTOR_DATABASE`
- `PGVECTOR_USER`
- `PGVECTOR_PASSWORD`
- `PGVECTOR_TABLE`
- `PGVECTOR_DIMENSION`

## 核心 Bean

`LangChainConfiguration` 创建了以下 Bean：

- `ChatLanguageModel`：标准通义千问对话模型
- `StreamingChatLanguageModel`：流式输出模型
- `EmbeddingStore<TextSegment>`：PGVector 向量库
- `ConversationalRetrievalChain`：多轮检索增强对话

## 服务封装

`HealthConsultationChatService` 提供基础的对话调用，后续可在该服务中：

- 注入医疗知识库检索逻辑
- 对接 `health_consultation`、`consultation_message` 表保存会话
- 加入健康风险提示与免责声明

已提供 `POST /api/ai/consult/chat` 示例接口，前端可直接对接实现健康咨询功能。

## 下一步

1. 为 `ChatMemoryStore` 提供 Redis/数据库实现，支持多实例共享记忆。
2. 构建医疗知识文档→向量化→存储流程，可使用 batch 作业。
3. 在 Controller 层暴露 `/api/ai/consult` 等接口，支持流式输出。
4. 增加调用鉴权与速率限制，防止恶意滥用。
