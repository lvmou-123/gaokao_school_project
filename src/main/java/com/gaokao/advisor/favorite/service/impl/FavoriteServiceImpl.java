package com.gaokao.advisor.favorite.service.impl;

import com.gaokao.advisor.common.exception.BusinessException;
import com.gaokao.advisor.common.exception.ErrorCode;
import com.gaokao.advisor.common.model.PageResult;
import com.gaokao.advisor.favorite.dto.FavoriteSchoolResponse;
import com.gaokao.advisor.favorite.dto.FavoriteStatusResponse;
import com.gaokao.advisor.favorite.entity.SchoolFavorite;
import com.gaokao.advisor.favorite.repository.FavoriteRepository;
import com.gaokao.advisor.favorite.service.FavoriteService;
import com.gaokao.advisor.school.entity.School;
import com.gaokao.advisor.school.repository.SchoolRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final SchoolRepository schoolRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addFavorite(Long userId, Long schoolId) {
        if (favoriteRepository.existsByUserIdAndSchoolId(userId, schoolId)) {
            throw new BusinessException(ErrorCode.CONFLICT, "已收藏该院校");
        }
        if (!schoolRepository.existsById(schoolId)) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "院校不存在");
        }

        SchoolFavorite favorite = new SchoolFavorite();
        favorite.setUserId(userId);
        favorite.setSchoolId(schoolId);
        favoriteRepository.save(favorite);
        log.info("User {} favorited school {}", userId, schoolId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeFavorite(Long userId, Long schoolId) {
        SchoolFavorite favorite = favoriteRepository.findByUserIdAndSchoolId(userId, schoolId)
                .orElseThrow(() -> new BusinessException("未收藏该院校"));
        favoriteRepository.delete(favorite);
        log.info("User {} unfavorited school {}", userId, schoolId);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult<FavoriteSchoolResponse> listFavorites(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<SchoolFavorite> favoritePage = favoriteRepository.findByUserId(userId, pageable);

        if (favoritePage.getContent().isEmpty()) {
            return new PageResult<>(List.of(), 0, page, size);
        }

        List<Long> schoolIds = favoritePage.getContent().stream()
                .map(SchoolFavorite::getSchoolId)
                .toList();

        Map<Long, School> schoolMap = schoolRepository.findAllById(schoolIds).stream()
                .collect(Collectors.toMap(School::getId, s -> s));

        List<FavoriteSchoolResponse> list = favoritePage.getContent().stream()
                .map(fav -> {
                    School school = schoolMap.get(fav.getSchoolId());
                    if (school == null) return null;
                    FavoriteSchoolResponse resp = new FavoriteSchoolResponse();
                    resp.setFavoriteId(fav.getId());
                    resp.setSchoolId(school.getId());
                    resp.setSchoolName(school.getName());
                    resp.setProvince(school.getProvince());
                    resp.setCity(school.getCity());
                    resp.setTags(new ArrayList<>(school.getTags()));
                    resp.setCategory(school.getCategory());
                    resp.setLogo(school.getLogo());
                    resp.setCreatedAt(fav.getCreatedAt().toString());
                    return resp;
                })
                .filter(r -> r != null)
                .toList();

        return new PageResult<>(list, favoritePage.getTotalElements(), page, size);
    }

    @Override
    @Transactional(readOnly = true)
    public FavoriteStatusResponse checkFavorite(Long userId, Long schoolId) {
        FavoriteStatusResponse resp = new FavoriteStatusResponse();
        favoriteRepository.findByUserIdAndSchoolId(userId, schoolId).ifPresentOrElse(
                fav -> {
                    resp.setFavorited(true);
                    resp.setFavoriteId(fav.getId());
                },
                () -> resp.setFavorited(false)
        );
        return resp;
    }
}
