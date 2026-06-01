package com.gaokao.advisor.major.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "院校简要信息")
public class SchoolBrief {

    @Schema(description = "院校ID", example = "1")
    private Long id;

    @Schema(description = "院校名称", example = "郑州大学")
    private String name;
}
