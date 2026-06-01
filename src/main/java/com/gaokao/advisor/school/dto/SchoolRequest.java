package com.gaokao.advisor.school.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.List;

@Data
@Schema(description = "新增院校请求")
public class SchoolRequest {

    @NotBlank(message = "院校名称不能为空")
    @Schema(description = "院校名称", example = "郑州大学")
    private String name;

    @Schema(description = "院校代码", example = "10459")
    private String code;

    @Schema(description = "所在省份", example = "河南")
    private String province;

    @Schema(description = "所在城市", example = "郑州")
    private String city;

    @Schema(description = "院校标签（如：985、211、双一流）")
    private List<String> tags;

    @Schema(description = "院校类别", example = "综合")
    private String category;

    @Schema(description = "院校简介")
    private String description;
}
