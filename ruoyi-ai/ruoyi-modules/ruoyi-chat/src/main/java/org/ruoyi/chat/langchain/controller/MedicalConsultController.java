package org.ruoyi.chat.langchain.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.ruoyi.chat.langchain.service.HealthConsultationChatService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai/consult")
@RequiredArgsConstructor
public class MedicalConsultController {

    private final HealthConsultationChatService chatService;

    @PostMapping("/chat")
    public ConsultResponse chat(@Valid @RequestBody ConsultRequest request) {
        String answer = chatService.chat(request.getConsultationId(), request.getQuestion());
        ConsultResponse response = new ConsultResponse();
        response.setAnswer(answer);
        return response;
    }

    @Data
    public static class ConsultRequest {
        private Long consultationId;

        @NotBlank(message = "问题描述不能为空")
        private String question;
    }

    @Data
    public static class ConsultResponse {
        private String answer;
    }
}
