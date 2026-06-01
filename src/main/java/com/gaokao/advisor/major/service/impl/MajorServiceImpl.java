package com.gaokao.advisor.major.service.impl;

import com.gaokao.advisor.common.exception.BusinessException;
import com.gaokao.advisor.common.model.PageResult;
import com.gaokao.advisor.major.dto.MajorRequest;
import com.gaokao.advisor.major.dto.MajorResponse;
import com.gaokao.advisor.major.dto.SchoolBrief;
import com.gaokao.advisor.major.entity.Major;
import com.gaokao.advisor.major.repository.MajorRepository;
import com.gaokao.advisor.major.service.MajorService;
import com.gaokao.advisor.school.entity.School;
import com.gaokao.advisor.school.repository.SchoolRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MajorServiceImpl implements MajorService {

    private final MajorRepository majorRepository;
    private final SchoolRepository schoolRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MajorResponse create(MajorRequest request) {
        School school = schoolRepository.findById(request.getSchoolId())
                .orElseThrow(() -> new BusinessException("所属院校不存在"));

        Major major = new Major();
        major.setName(request.getName());
        major.setCode(request.getCode());
        major.setCategory(request.getCategory());
        major.setDescription(request.getDescription());
        major.setDuration(request.getDuration());
        major.setDegree(request.getDegree());
        major.setGenderRatio(request.getGenderRatio());
        major.setAvgSalary(request.getAvgSalary());

        majorRepository.save(major);

        major.getSchools().add(school);
        majorRepository.save(major);
        log.info("Major created: {} for school id={}", major.getName(), school.getId());

        return toResponse(major);
    }

    @Override
    public MajorResponse getById(Long id) {
        Major major = majorRepository.findById(id)
                .orElseThrow(() -> new BusinessException("专业不存在"));
        return toResponse(major);
    }

    @Override
    public List<MajorResponse> listBySchool(Long schoolId) {
        return majorRepository.findBySchoolId(schoolId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public PageResult<MajorResponse> search(String keyword, String category, int page, int size) {
        Specification<Major> spec = Specification.where(null);

        if (keyword != null && !keyword.isBlank()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(root.get("name"), "%" + keyword.trim() + "%"));
        }

        if (category != null && !category.isBlank()) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("category"), category.trim()));
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));
        Page<Major> majorPage = majorRepository.findAll(spec, pageable);

        List<MajorResponse> list = majorPage.getContent().stream()
                .map(this::toResponse)
                .toList();

        return new PageResult<>(list, majorPage.getTotalElements(), page, size);
    }

    private MajorResponse toResponse(Major major) {
        MajorResponse response = new MajorResponse();
        response.setId(major.getId());
        response.setName(major.getName());
        response.setCode(major.getCode());
        response.setCategory(major.getCategory());
        response.setDescription(major.getDescription());
        response.setSchools(major.getSchools().stream()
                .map(s -> new SchoolBrief(s.getId(), s.getName()))
                .toList());
        response.setDuration(major.getDuration());
        response.setDegree(major.getDegree());
        response.setGenderRatio(major.getGenderRatio());
        response.setAvgSalary(major.getAvgSalary());
        return response;
    }
}
