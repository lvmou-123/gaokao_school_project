package com.gaokao.advisor.ai.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "ai")
public class AiProperties {

    private Zhipu zhipu = new Zhipu();

    @Data
    public static class Zhipu {
        private String apiKey;
        private String model = "glm-4.5";
        private String baseUrl = "https://open.bigmodel.cn/api/paas/v4";
        private String systemPrompt = """
                你是一个高考志愿填报专家助手，你的职责是根据用户提供的高考分数、排名、省份、偏好等信息，
                为用户提供个性化的高考志愿填报建议。请基于中国高校的实际情况给出建议，
                包括冲刺院校、稳妥院校和保底院校三个梯度的推荐。
                回答应简洁、实用、有针对性。不要回答与高考志愿无关的问题。
                """;
    }
}
