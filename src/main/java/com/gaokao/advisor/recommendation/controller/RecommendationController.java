package com.gaokao.advisor.recommendation.controller;

import com.gaokao.advisor.common.model.Result;
import com.gaokao.advisor.recommendation.dto.RecommendRequest;
import com.gaokao.advisor.recommendation.dto.RecommendResponse;
import com.gaokao.advisor.recommendation.service.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
@Tag(name = "院校推荐", description = "基于用户成绩和偏好生成个性化院校推荐列表")
public class RecommendationController {

    private final RecommendationService recommendationService;

    @PostMapping
    @Operation(summary = "智能推荐", description = "根据用户成绩、排名、省份和偏好生成冲刺/稳妥/保底三个梯度的院校推荐")
    public Result<List<RecommendResponse>> recommend(@Valid @RequestBody RecommendRequest request) {
        return Result.success(recommendationService.recommend(request));
    }

    @GetMapping("/history/{userId}")
    @Operation(summary = "推荐历史", description = "查询指定用户的推荐历史记录")
    public Result<List<RecommendResponse>> history(@PathVariable Long userId) {
        return Result.success(recommendationService.getHistory(userId));
    }
}
