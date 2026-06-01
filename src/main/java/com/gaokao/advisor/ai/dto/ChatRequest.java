package com.gaokao.advisor.ai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "AI 对话请求")
public class ChatRequest {

    @NotBlank(message = "问题不能为空")
    @Size(max = 2000, message = "问题长度不能超过2000字")
    @Schema(description = "用户咨询的问题", example = "河南理科600分能报哪些大学？")
    private String message;

    @Schema(description = "用户ID（可选，携带后可获取个性化建议）")
    private String userId;

    @Schema(description = "高考总分", example = "600")
    private Integer score;

    @Schema(description = "全省排名", example = "15000")
    private Integer rankNum;

    @Schema(description = "所在省份", example = "河南")
    private String province;
}
