package org.ruoyi.chat.langchain.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "langchain.qwen")
public class QwenProperties {

    /**
     * 阿里百炼 API Key。
     */
    @NotBlank(message = "通义千问 API Key 不能为空")
    private String apiKey;

    /**
     * 模型名称，如 qwen-plus。
     */
    @NotBlank(message = "通义千问模型名称不能为空")
    private String model;

    /**
     * 自定义服务地址，可选。
     */
    private String endpoint;

    /**
     * 温度参数，默认 0.7。
     */
    private Double temperature = 0.7;

    /**
     * topP 采样参数，默认 0.95。
     */
    private Double topP = 0.95;

    /**
     * 最大 token 数，默认 2048。
     */
    private Integer maxTokens = 2048;

}
