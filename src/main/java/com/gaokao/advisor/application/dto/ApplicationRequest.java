package com.gaokao.advisor.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "添加志愿请求")
public class ApplicationRequest {

    @NotNull(message = "用户ID不能为空")
    @Schema(description = "用户ID", example = "1")
    private Long userId;

    @NotNull(message = "院校ID不能为空")
    @Schema(description = "院校ID", example = "1")
    private Long schoolId;

    @Schema(description = "专业ID（可选）", example = "1")
    private Long majorId;

    @NotNull(message = "志愿序号不能为空")
    @Schema(description = "志愿优先级（1-10）", example = "1")
    private Integer priority;
}
