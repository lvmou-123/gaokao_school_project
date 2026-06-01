package com.gaokao.advisor.user.service.impl;

import com.gaokao.advisor.common.exception.BusinessException;
import com.gaokao.advisor.user.dto.UserRequest;
import com.gaokao.advisor.user.dto.UserResponse;
import com.gaokao.advisor.user.dto.UserScoreRequest;
import com.gaokao.advisor.user.entity.User;
import com.gaokao.advisor.user.repository.UserRepository;
import com.gaokao.advisor.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserResponse register(UserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException("用户名已存在");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRealName(request.getRealName());
        user.setPhone(request.getPhone());
        user.setProvince(request.getProvince());
        user.setExamType(request.getExamType());
        user.setGraduationYear(request.getGraduationYear());

        userRepository.save(user);
        log.info("User registered: {}", user.getUsername());

        return toResponse(user);
    }

    @Override
    public UserResponse getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("用户不存在"));
        return toResponse(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserResponse updateScores(Long id, UserScoreRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("用户不存在"));

        if (request.getTotalScore() != null) {
            user.setTotalScore(request.getTotalScore());
        }
        if (request.getRankNum() != null) {
            user.setRankNum(request.getRankNum());
        }
        if (request.getExamType() != null) {
            user.setExamType(request.getExamType());
        }
        if (request.getProvince() != null) {
            user.setProvince(request.getProvince());
        }
        if (request.getGraduationYear() != null) {
            user.setGraduationYear(request.getGraduationYear());
        }

        userRepository.save(user);
        log.info("User scores updated: id={}, score={}, rank={}", id, user.getTotalScore(), user.getRankNum());

        return toResponse(user);
    }

    private UserResponse toResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setRealName(user.getRealName());
        response.setPhone(user.getPhone());
        response.setProvince(user.getProvince());
        response.setTotalScore(user.getTotalScore());
        response.setRankNum(user.getRankNum());
        return response;
    }
}
