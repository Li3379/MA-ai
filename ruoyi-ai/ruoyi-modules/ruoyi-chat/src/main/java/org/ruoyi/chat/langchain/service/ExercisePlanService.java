package org.ruoyi.chat.langchain.service;

import dev.langchain4j.model.chat.ChatLanguageModel;
import org.springframework.stereotype.Service;

@Service
public class ExercisePlanService {

    private final ChatLanguageModel chatLanguageModel;

    public ExercisePlanService(ChatLanguageModel chatLanguageModel) {
        this.chatLanguageModel = chatLanguageModel;
    }

    public String generatePlan(String prompt) {
        return chatLanguageModel.generate(prompt);
    }
}
