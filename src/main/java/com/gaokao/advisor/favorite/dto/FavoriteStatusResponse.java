package com.gaokao.advisor.favorite.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "收藏状态")
public class FavoriteStatusResponse {

    @Schema(description = "是否已收藏")
    private boolean favorited;

    @Schema(description = "收藏记录ID")
    private Long favoriteId;
}
