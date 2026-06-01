package com.gaokao.advisor.major.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "新增专业请求")
public class MajorRequest {

    @NotBlank(message = "专业名称不能为空")
    @Schema(description = "专业名称", example = "计算机科学与技术")
    private String name;

    @Schema(description = "专业代码", example = "080901")
    private String code;

    @Schema(description = "学科门类", example = "工学")
    private String category;

    @Schema(description = "专业描述")
    private String description;

    @NotNull(message = "所属院校不能为空")
    @Schema(description = "所属院校ID", example = "1")
    private Long schoolId;

    @Schema(description = "学制", example = "四年")
    private String duration;

    @Schema(description = "授予学位", example = "工学学士")
    private String degree;

    @Schema(description = "性别比例", example = "男:女 7:3")
    private String genderRatio;

    @Schema(description = "平均薪资", example = "¥12,000/月")
    private String avgSalary;
}
