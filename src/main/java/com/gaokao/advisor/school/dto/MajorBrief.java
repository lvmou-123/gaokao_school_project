package com.gaokao.advisor.school.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "专业简要信息")
public class MajorBrief {

    @Schema(description = "专业ID", example = "1")
    private Long id;

    @Schema(description = "专业名称", example = "计算机科学与技术")
    private String name;
}
