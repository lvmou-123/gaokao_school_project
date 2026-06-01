package com.gaokao.advisor.user.service;

import com.gaokao.advisor.user.dto.UserRequest;
import com.gaokao.advisor.user.dto.UserResponse;
import com.gaokao.advisor.user.dto.UserScoreRequest;

public interface UserService {

    UserResponse register(UserRequest request);

    UserResponse getById(Long id);

    UserResponse updateScores(Long id, UserScoreRequest request);
}
