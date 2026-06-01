package com.gaokao.advisor.school.service.impl;

import com.gaokao.advisor.common.exception.BusinessException;
import com.gaokao.advisor.common.model.PageResult;
import com.gaokao.advisor.major.entity.Major;
import com.gaokao.advisor.major.repository.MajorRepository;
import com.gaokao.advisor.school.dto.MajorBrief;
import com.gaokao.advisor.school.dto.SchoolRequest;
import com.gaokao.advisor.school.dto.SchoolResponse;
import com.gaokao.advisor.school.entity.School;
import com.gaokao.advisor.school.repository.SchoolRepository;
import com.gaokao.advisor.school.service.SchoolService;
import jakarta.persistence.criteria.Join;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchoolServiceImpl implements SchoolService {

    private final SchoolRepository schoolRepository;
    private final MajorRepository majorRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SchoolResponse create(SchoolRequest request) {
        School school = new School();
        school.setName(request.getName());
        school.setCode(request.getCode());
        school.setProvince(request.getProvince());
        school.setCity(request.getCity());
        if (request.getTags() != null) {
            school.setTags(new java.util.HashSet<>(request.getTags()));
        }
        school.setCategory(request.getCategory());
        school.setDescription(request.getDescription());

        schoolRepository.save(school);
        log.info("School created: {}", school.getName());

        return toResponse(school);
    }

    @Override
    @Transactional(readOnly = true)
    public SchoolResponse getById(Long id) {
        School school = schoolRepository.findById(id)
                .orElseThrow(() -> new BusinessException("院校不存在"));
        return toResponse(school);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult<SchoolResponse> search(String keyword, String province, String tag, int page, int size) {
        Specification<School> spec = buildSpecification(keyword, province, tag);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));
        Page<School> schoolPage = schoolRepository.findAll(spec, pageable);

        List<SchoolResponse> list = schoolPage.getContent().stream()
                .map(this::toResponse)
                .toList();

        return new PageResult<>(list, schoolPage.getTotalElements(), page, size);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult<SchoolResponse> getSchoolsByMajor(Long majorId, int page, int size) {
        Major major = majorRepository.findById(majorId)
                .orElseThrow(() -> new BusinessException("专业不存在"));

        List<Long> schoolIds = major.getSchools().stream()
                .map(School::getId)
                .toList();

        if (schoolIds.isEmpty()) {
            return new PageResult<>(List.of(), 0, page, size);
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));
        Page<School> schoolPage = schoolRepository.findByIdIn(schoolIds, pageable);

        List<SchoolResponse> list = schoolPage.getContent().stream()
                .map(this::toResponse)
                .toList();

        return new PageResult<>(list, schoolPage.getTotalElements(), page, size);
    }

    private Specification<School> buildSpecification(String keyword, String province, String tag) {
        Specification<School> spec = Specification.where(null);

        if (keyword != null && !keyword.isBlank()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(root.get("name"), "%" + keyword.trim() + "%"));
        }

        if (province != null && !province.isBlank()) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("province"), province.trim()));
        }

        if (tag != null && !tag.isBlank()) {
            spec = spec.and((root, query, cb) -> {
                Join<School, String> join = root.join("tags");
                return cb.equal(join, tag.trim());
            });
        }

        return spec;
    }

    private SchoolResponse toResponse(School school) {
        SchoolResponse response = new SchoolResponse();
        response.setId(school.getId());
        response.setName(school.getName());
        response.setCode(school.getCode());
        response.setProvince(school.getProvince());
        response.setCity(school.getCity());
        response.setTags(new ArrayList<>(school.getTags()));
        response.setCategory(school.getCategory());
        response.setDescription(school.getDescription());
        response.setLogo(school.getLogo());
        response.setWebsite(school.getWebsite());
        response.setMajors(school.getMajors().stream()
                .map(m -> new MajorBrief(m.getId(), m.getName()))
                .toList());
        return response;
    }
}
