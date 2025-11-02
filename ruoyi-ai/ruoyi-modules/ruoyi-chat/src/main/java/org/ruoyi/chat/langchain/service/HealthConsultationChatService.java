package org.ruoyi.chat.langchain.service;

import dev.langchain4j.chain.ConversationalRetrievalChain;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class HealthConsultationChatService {

    private final ConversationalRetrievalChain conversationalRetrievalChain;

    public HealthConsultationChatService(@NonNull ConversationalRetrievalChain conversationalRetrievalChain) {
        this.conversationalRetrievalChain = conversationalRetrievalChain;
    }

    /**
     * 调用通义千问对话模型，后续可接入数据库保存上下文。
     *
     * @param consultationId 会话ID，可用于记录数据库
     * @param userInput      用户问题
     * @return AI 回复
     */
    public String chat(Long consultationId, String userInput) {
        return conversationalRetrievalChain.execute(userInput);
    }
}
