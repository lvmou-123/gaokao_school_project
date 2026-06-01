package com.gaokao.advisor.recommendation.service.impl;

import com.gaokao.advisor.common.exception.BusinessException;
import com.gaokao.advisor.recommendation.dto.RecommendRequest;
import com.gaokao.advisor.recommendation.dto.RecommendResponse;
import com.gaokao.advisor.recommendation.entity.Recommendation;
import com.gaokao.advisor.recommendation.repository.RecommendationRepository;
import com.gaokao.advisor.recommendation.service.RecommendationService;
import com.gaokao.advisor.school.entity.School;
import com.gaokao.advisor.school.repository.SchoolRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {

    private final RecommendationRepository recommendationRepository;
    private final SchoolRepository schoolRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<RecommendResponse> recommend(RecommendRequest request) {
        log.info("Generating recommendations for user id={}, score={}, rank={}",
                request.getUserId(), request.getScore(), request.getRankNum());

        String province = request.getProvince();
        List<String> preferences = request.getPreferences();

        if (province == null || province.isBlank()) {
            throw new BusinessException("省份不能为空");
        }

        List<School> schools;
        if (preferences != null && !preferences.isEmpty()) {
            Set<String> prefSet = preferences.stream().map(String::trim)
                    .collect(Collectors.toSet());
            schools = schoolRepository.findByProvince(province).stream()
                    .filter(s -> s.getCategory() != null && prefSet.contains(s.getCategory()))
                    .collect(Collectors.toList());
        } else {
            schools = schoolRepository.findByProvince(province);
        }

        if (schools.isEmpty()) {
            throw new BusinessException("未找到符合条件的院校");
        }

        int total = schools.size();
        int safeCount = Math.max(1, total / 3);
        int stableCount = Math.max(1, total / 3);

        List<RecommendResponse> results = new ArrayList<>();
        for (int i = 0; i < total && i < 15; i++) {
            School school = schools.get(i);

            String probability;
            String reason;
            if (i < safeCount) {
                probability = "保底";
                reason = "该院校录取概率较高，适合作为保底选择";
            } else if (i < safeCount + stableCount) {
                probability = "稳妥";
                reason = "该院校与您的成绩匹配度较高，适合作为稳妥选择";
            } else {
                probability = "冲刺";
                reason = "该院校录取有一定挑战，但值得尝试冲刺";
            }

            Recommendation rec = new Recommendation();
            rec.setUserId(request.getUserId());
            rec.setSchoolId(school.getId());
            rec.setProbability(probability);
            rec.setReason(reason);
            recommendationRepository.save(rec);

            RecommendResponse resp = new RecommendResponse();
            resp.setId(rec.getId());
            resp.setSchoolId(school.getId());
            resp.setSchoolName(school.getName());
            resp.setProbability(probability);
            resp.setReason(reason);
            results.add(resp);
        }

        log.info("Generated {} recommendations for user {}", results.size(), request.getUserId());
        return results;
    }

    @Override
    public List<RecommendResponse> getHistory(Long userId) {
        return recommendationRepository.findByUserId(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    private RecommendResponse toResponse(Recommendation rec) {
        RecommendResponse response = new RecommendResponse();
        response.setId(rec.getId());
        response.setSchoolId(rec.getSchoolId());
        response.setMajorId(rec.getMajorId());
        response.setProbability(rec.getProbability());
        response.setReason(rec.getReason());
        schoolRepository.findById(rec.getSchoolId()).ifPresent(s ->
                response.setSchoolName(s.getName()));
        return response;
    }
}
