package org.ruoyi.chat.langchain.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.ruoyi.chat.langchain.service.ExercisePlanService;
import org.ruoyi.common.core.web.domain.AjaxResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai/exercise")
@RequiredArgsConstructor
public class ExercisePlanController {

    private final ExercisePlanService exercisePlanService;

    @PostMapping("/plan")
    public AjaxResult generatePlan(@Valid @RequestBody ExercisePlanRequest request) {
        String prompt = buildPrompt(request);
        String answer = exercisePlanService.generatePlan(prompt);
        return AjaxResult.success(new ExercisePlanResponse(answer));
    }

    private String buildPrompt(ExercisePlanRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append("你是一名专业的运动康复教练，需要为用户生成个性化运动方案。请使用 JSON 格式输出：\\n");
        sb.append("{\\n");
        sb.append("  \"summary\": \"整体方案概述\",\\n");
        sb.append("  \"weekly_schedule\": [\\n");
        String[] weekDays = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        int days = Math.min(Math.max(request.getAvailableDays(), 1), 7);
        for (int i = 0; i < days; i++) {
            sb.append("    { \"day\": \"").append(weekDays[i]).append("\", \"activities\": [] }");
            if (i < days - 1) {
                sb.append(",");
            }
            sb.append("\\n");
        }
        sb.append("  ],\\n");
        sb.append("  \"precautions\": []\\n");
        sb.append("}\\n");
        sb.append("用户信息如下：\\n");
        sb.append("- 年龄：").append(request.getAge()).append("\\n");
        sb.append("- 身高：").append(request.getHeight()).append("cm\\n");
        sb.append("- 体重：").append(request.getWeight()).append("kg\\n");
        sb.append("- 慢性病史：").append(request.getChronicDiseases()).append("\\n");
        sb.append("- 运动目标：").append(request.getGoal()).append("\\n");
        sb.append("- 每周可用时间：").append(request.getAvailableDays()).append("天\\n");
        return sb.toString();
    }

    @Data
    public static class ExercisePlanRequest {
        @NotNull(message = "年龄不能为空")
        @Min(value = 1, message = "年龄至少为 1")
        @Max(value = 120, message = "年龄不能超过 120")
        private Integer age;

        @NotNull(message = "身高不能为空")
        private Double height;

        @NotNull(message = "体重不能为空")
        private Double weight;

        private String chronicDiseases;

        @NotBlank(message = "请填写运动目标")
        private String goal;

        @NotNull(message = "每周训练天数不能为空")
        @Min(value = 1, message = "每周至少 1 天")
        @Max(value = 7, message = "每周最多 7 天")
        private Integer availableDays;
    }

    public record ExercisePlanResponse(String content) {
    }
}
