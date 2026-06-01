package com.gaokao.advisor.favorite.controller;

import com.gaokao.advisor.common.model.PageResult;
import com.gaokao.advisor.common.model.Result;
import com.gaokao.advisor.favorite.dto.FavoriteSchoolResponse;
import com.gaokao.advisor.favorite.dto.FavoriteStatusResponse;
import com.gaokao.advisor.favorite.service.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/favorites/schools")
@RequiredArgsConstructor
@Tag(name = "院校收藏", description = "用户收藏/取消收藏院校，查看收藏列表")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("/{schoolId}")
    @Operation(summary = "收藏院校", description = "收藏指定的院校")
    public Result<Void> addFavorite(@Parameter(description = "院校ID") @PathVariable Long schoolId,
                                    HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        favoriteService.addFavorite(userId, schoolId);
        return Result.success();
    }

    @DeleteMapping("/{schoolId}")
    @Operation(summary = "取消收藏", description = "取消收藏指定的院校")
    public Result<Void> removeFavorite(@Parameter(description = "院校ID") @PathVariable Long schoolId,
                                       HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        favoriteService.removeFavorite(userId, schoolId);
        return Result.success();
    }

    @GetMapping
    @Operation(summary = "收藏列表", description = "分页获取当前用户的院校收藏列表")
    public Result<PageResult<FavoriteSchoolResponse>> listFavorites(
            @Parameter(description = "页码（从0开始）") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页条数") @RequestParam(defaultValue = "20") int size,
            HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        return Result.success(favoriteService.listFavorites(userId, page, size));
    }

    @GetMapping("/check/{schoolId}")
    @Operation(summary = "检查收藏状态", description = "检查当前用户是否已收藏指定的院校")
    public Result<FavoriteStatusResponse> checkFavorite(
            @Parameter(description = "院校ID") @PathVariable Long schoolId,
            HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        return Result.success(favoriteService.checkFavorite(userId, schoolId));
    }

    private Long getCurrentUserId(HttpServletRequest request) {
        return (Long) request.getAttribute("currentUserId");
    }
}
