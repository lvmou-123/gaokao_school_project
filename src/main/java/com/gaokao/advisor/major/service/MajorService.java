package com.gaokao.advisor.major.service;

import com.gaokao.advisor.common.model.PageResult;
import com.gaokao.advisor.major.dto.MajorRequest;
import com.gaokao.advisor.major.dto.MajorResponse;

import java.util.List;

public interface MajorService {

    MajorResponse create(MajorRequest request);

    MajorResponse getById(Long id);

    List<MajorResponse> listBySchool(Long schoolId);

    PageResult<MajorResponse> search(String keyword, String category, int page, int size);
}
