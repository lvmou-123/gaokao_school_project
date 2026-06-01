package com.gaokao.advisor.favorite.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.List;

@Data
@Schema(description = "收藏院校信息")
public class FavoriteSchoolResponse {

    @Schema(description = "收藏记录ID")
    private Long favoriteId;

    @Schema(description = "院校ID")
    private Long schoolId;

    @Schema(description = "院校名称")
    private String schoolName;

    @Schema(description = "所在省份")
    private String province;

    @Schema(description = "所在城市")
    private String city;

    @Schema(description = "院校标签列表")
    private List<String> tags;

    @Schema(description = "院校类别", example = "综合")
    private String category;

    @Schema(description = "Logo URL")
    private String logo;

    @Schema(description = "收藏时间")
    private String createdAt;
}
