package com.gaokao.advisor.application.service.impl;

import com.gaokao.advisor.application.dto.ApplicationRequest;
import com.gaokao.advisor.application.dto.ApplicationResponse;
import com.gaokao.advisor.application.entity.Application;
import com.gaokao.advisor.application.repository.ApplicationRepository;
import com.gaokao.advisor.application.service.ApplicationService;
import com.gaokao.advisor.common.exception.BusinessException;
import com.gaokao.advisor.major.entity.Major;
import com.gaokao.advisor.major.repository.MajorRepository;
import com.gaokao.advisor.school.entity.School;
import com.gaokao.advisor.school.repository.SchoolRepository;
import com.gaokao.advisor.user.entity.User;
import com.gaokao.advisor.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final SchoolRepository schoolRepository;
    private final MajorRepository majorRepository;

    private static final Set<String> VALID_STATUSES = Set.of("draft", "submitted", "admitted");

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApplicationResponse create(ApplicationRequest request) {
        userRepository.findById(request.getUserId())
                .orElseThrow(() -> new BusinessException("用户不存在"));
        schoolRepository.findById(request.getSchoolId())
                .orElseThrow(() -> new BusinessException("院校不存在"));
        if (request.getMajorId() != null) {
            majorRepository.findById(request.getMajorId())
                    .orElseThrow(() -> new BusinessException("专业不存在"));
        }

        Application app = new Application();
        app.setUserId(request.getUserId());
        app.setSchoolId(request.getSchoolId());
        app.setMajorId(request.getMajorId());
        app.setPriority(request.getPriority());
        app.setStatus("draft");

        applicationRepository.save(app);
        log.info("Application created: user={}, school={}, priority={}",
                request.getUserId(), request.getSchoolId(), request.getPriority());

        return toResponse(app);
    }

    @Override
    public List<ApplicationResponse> listByUser(Long userId) {
        return applicationRepository.findByUserIdOrderByPriorityAsc(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApplicationResponse updateStatus(Long id, String status) {
        if (status == null || !VALID_STATUSES.contains(status)) {
            throw new BusinessException("无效的状态值，仅支持: draft, submitted, admitted");
        }

        Application app = applicationRepository.findById(id)
                .orElseThrow(() -> new BusinessException("志愿不存在"));
        app.setStatus(status);
        applicationRepository.save(app);
        log.info("Application {} status updated to {}", id, status);
        return toResponse(app);
    }

    private ApplicationResponse toResponse(Application app) {
        ApplicationResponse response = new ApplicationResponse();
        response.setId(app.getId());
        response.setUserId(app.getUserId());
        response.setSchoolId(app.getSchoolId());
        response.setMajorId(app.getMajorId());
        response.setPriority(app.getPriority());
        response.setStatus(app.getStatus());
        response.setCreatedAt(app.getCreatedAt());

        schoolRepository.findById(app.getSchoolId())
                .ifPresent(s -> response.setSchoolName(s.getName()));
        if (app.getMajorId() != null) {
            majorRepository.findById(app.getMajorId())
                    .ifPresent(m -> response.setMajorName(m.getName()));
        }

        return response;
    }
}
