package com.gaokao.advisor.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "志愿信息响应")
public class ApplicationResponse {

    @Schema(description = "志愿ID", example = "1")
    private Long id;

    @Schema(description = "用户ID", example = "1")
    private Long userId;

    @Schema(description = "院校ID", example = "1")
    private Long schoolId;

    @Schema(description = "院校名称", example = "郑州大学")
    private String schoolName;

    @Schema(description = "专业ID", example = "1")
    private Long majorId;

    @Schema(description = "专业名称", example = "计算机科学与技术")
    private String majorName;

    @Schema(description = "志愿优先级（1-10）", example = "1")
    private Integer priority;

    @Schema(description = "状态：draft-草稿 / submitted-已提交 / admitted-已录取", example = "draft")
    private String status;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
}
