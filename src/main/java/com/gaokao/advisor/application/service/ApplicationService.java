package com.gaokao.advisor.application.service;

import com.gaokao.advisor.application.dto.ApplicationRequest;
import com.gaokao.advisor.application.dto.ApplicationResponse;

import java.util.List;

public interface ApplicationService {

    ApplicationResponse create(ApplicationRequest request);

    List<ApplicationResponse> listByUser(Long userId);

    ApplicationResponse updateStatus(Long id, String status);
}
