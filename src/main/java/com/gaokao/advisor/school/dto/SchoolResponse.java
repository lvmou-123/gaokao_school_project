package com.gaokao.advisor.school.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.List;

@Data
@Schema(description = "院校信息响应")
public class SchoolResponse {

    @Schema(description = "院校ID", example = "1")
    private Long id;

    @Schema(description = "院校名称", example = "郑州大学")
    private String name;

    @Schema(description = "院校代码", example = "10459")
    private String code;

    @Schema(description = "所在省份", example = "河南")
    private String province;

    @Schema(description = "所在城市", example = "郑州")
    private String city;

    @Schema(description = "院校标签列表")
    private List<String> tags;

    @Schema(description = "院校类别", example = "综合")
    private String category;

    @Schema(description = "院校简介")
    private String description;

    @Schema(description = "Logo URL")
    private String logo;

    @Schema(description = "官网地址")
    private String website;

    @Schema(description = "开设专业列表")
    private List<MajorBrief> majors;
}
