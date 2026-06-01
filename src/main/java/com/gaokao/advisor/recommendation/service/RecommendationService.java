package com.gaokao.advisor.recommendation.service;

import com.gaokao.advisor.recommendation.dto.RecommendRequest;
import com.gaokao.advisor.recommendation.dto.RecommendResponse;

import java.util.List;

public interface RecommendationService {

    List<RecommendResponse> recommend(RecommendRequest request);

    List<RecommendResponse> getHistory(Long userId);
}
