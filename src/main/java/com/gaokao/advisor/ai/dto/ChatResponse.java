package com.gaokao.advisor.ai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "AI 对话响应")
public class ChatResponse {

    @Schema(description = "AI回复内容", example = "根据您的分数和排名，建议冲刺郑州大学...")
    private String reply;

    @Schema(description = "AI模型名称", example = "glm-4.5")
    private String model;
}
