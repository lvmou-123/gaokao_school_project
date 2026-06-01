package com.gaokao.advisor.school.service;

import com.gaokao.advisor.common.model.PageResult;
import com.gaokao.advisor.school.dto.SchoolRequest;
import com.gaokao.advisor.school.dto.SchoolResponse;

public interface SchoolService {

    SchoolResponse create(SchoolRequest request);

    SchoolResponse getById(Long id);

    PageResult<SchoolResponse> search(String keyword, String province, String tag, int page, int size);

    PageResult<SchoolResponse> getSchoolsByMajor(Long majorId, int page, int size);
}
