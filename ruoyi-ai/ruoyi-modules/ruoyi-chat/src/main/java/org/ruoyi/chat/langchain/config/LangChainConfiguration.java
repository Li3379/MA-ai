package org.ruoyi.chat.langchain.config;

import dev.langchain4j.chain.ConversationalRetrievalChain;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.qwen.QwenChatModel;
import dev.langchain4j.model.qwen.QwenStreamingChatModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.ChatMemoryStore;
import dev.langchain4j.memory.chat.InMemoryChatMemoryStore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
@EnableConfigurationProperties(QwenProperties.class)
public class LangChainConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ChatLanguageModel qwenChatLanguageModel(QwenProperties properties) {
        QwenChatModel.QwenChatModelBuilder builder = QwenChatModel.builder()
            .apiKey(properties.getApiKey())
            .modelName(properties.getModel());

        if (StringUtils.hasText(properties.getEndpoint())) {
            builder = builder.baseUrl(properties.getEndpoint());
        }

        if (properties.getTemperature() != null) {
            builder = builder.temperature(properties.getTemperature());
        }
        if (properties.getTopP() != null) {
            builder = builder.topP(properties.getTopP());
        }
        if (properties.getMaxTokens() != null) {
            builder = builder.maxTokens(properties.getMaxTokens());
        }

        return builder.build();
    }

    @Bean
    @ConditionalOnMissingBean
    public StreamingChatLanguageModel qwenStreamingChatLanguageModel(QwenProperties properties) {
        QwenStreamingChatModel.QwenStreamingChatModelBuilder builder = QwenStreamingChatModel.builder()
            .apiKey(properties.getApiKey())
            .modelName(properties.getModel());

        if (StringUtils.hasText(properties.getEndpoint())) {
            builder = builder.baseUrl(properties.getEndpoint());
        }

        if (properties.getTemperature() != null) {
            builder = builder.temperature(properties.getTemperature());
        }
        if (properties.getTopP() != null) {
            builder = builder.topP(properties.getTopP());
        }
        if (properties.getMaxTokens() != null) {
            builder = builder.maxTokens(properties.getMaxTokens());
        }

        return builder.build();
    }

    @Bean
    @ConditionalOnMissingBean
    public EmbeddingStore<TextSegment> pgVectorEmbeddingStore() {
        return PgVectorEmbeddingStore.builder()
            .host(System.getenv().getOrDefault("PGVECTOR_HOST", "localhost"))
            .port(Integer.parseInt(System.getenv().getOrDefault("PGVECTOR_PORT", "5432")))
            .database(System.getenv().getOrDefault("PGVECTOR_DATABASE", "ma_ai"))
            .user(System.getenv().getOrDefault("PGVECTOR_USER", "postgres"))
            .password(System.getenv().getOrDefault("PGVECTOR_PASSWORD", "postgres"))
            .schema(System.getenv().getOrDefault("PGVECTOR_SCHEMA", "public"))
            .table(System.getenv().getOrDefault("PGVECTOR_TABLE", "ai_knowledge_embeddings"))
            .dimension(Integer.parseInt(System.getenv().getOrDefault("PGVECTOR_DIMENSION", "1536")))
            .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public ChatMemoryStore chatMemoryStore() {
        return new InMemoryChatMemoryStore();
    }

    @Bean
    @ConditionalOnMissingBean
    public ConversationalRetrievalChain conversationalRetrievalChain(
        ChatLanguageModel qwenChatLanguageModel,
        EmbeddingStore<TextSegment> pgVectorEmbeddingStore
    ) {
        return ConversationalRetrievalChain.builder()
            .chatLanguageModel(qwenChatLanguageModel)
            .embeddingStore(pgVectorEmbeddingStore)
            .build();
    }
}
