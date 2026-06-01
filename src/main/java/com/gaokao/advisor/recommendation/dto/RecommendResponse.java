package com.gaokao.advisor.recommendation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "推荐结果响应")
public class RecommendResponse {

    @Schema(description = "推荐记录ID", example = "1")
    private Long id;

    @Schema(description = "院校ID", example = "1")
    private Long schoolId;

    @Schema(description = "院校名称", example = "郑州大学")
    private String schoolName;

    @Schema(description = "专业ID", example = "1")
    private Long majorId;

    @Schema(description = "专业名称", example = "计算机科学与技术")
    private String majorName;

    @Schema(description = "录取概率（冲刺/稳妥/保底）", example = "冲刺")
    private String probability;

    @Schema(description = "推荐理由", example = "该院校往年在河南录取位次约为12000名")
    private String reason;
}
