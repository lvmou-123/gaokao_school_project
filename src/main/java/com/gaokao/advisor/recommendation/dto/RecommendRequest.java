package com.gaokao.advisor.recommendation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "智能推荐请求")
public class RecommendRequest {

    @NotNull(message = "用户ID不能为空")
    @Schema(description = "用户ID", example = "1")
    private Long userId;

    @Schema(description = "高考总分", example = "600")
    private Integer score;

    @Schema(description = "全省排名", example = "15000")
    private Integer rankNum;

    @Schema(description = "所在省份", example = "河南")
    private String province;

    @Schema(description = "偏好标签（如：理工、综合、医药等）")
    private List<String> preferences;
}
