package com.gaokao.advisor.favorite.service;

import com.gaokao.advisor.common.model.PageResult;
import com.gaokao.advisor.favorite.dto.FavoriteSchoolResponse;
import com.gaokao.advisor.favorite.dto.FavoriteStatusResponse;

public interface FavoriteService {

    void addFavorite(Long userId, Long schoolId);

    void removeFavorite(Long userId, Long schoolId);

    PageResult<FavoriteSchoolResponse> listFavorites(Long userId, int page, int size);

    FavoriteStatusResponse checkFavorite(Long userId, Long schoolId);
}
