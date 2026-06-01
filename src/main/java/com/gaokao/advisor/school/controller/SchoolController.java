package com.gaokao.advisor.school.controller;

import com.gaokao.advisor.common.model.PageResult;
import com.gaokao.advisor.common.model.Result;
import com.gaokao.advisor.school.dto.SchoolRequest;
import com.gaokao.advisor.school.dto.SchoolResponse;
import com.gaokao.advisor.school.service.SchoolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schools")
@RequiredArgsConstructor
@Tag(name = "院校管理", description = "全国高等院校信息查询，支持按省份、标签、关键词筛选和分页")
public class SchoolController {

    private final SchoolService schoolService;

    @PostMapping
    @Operation(summary = "新增院校", description = "添加一所新的高等院校信息（管理端接口）")
    public Result<SchoolResponse> create(@Valid @RequestBody SchoolRequest request) {
        return Result.success(schoolService.create(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询院校详情", description = "根据院校ID获取详细信息，包含标签列表")
    public Result<SchoolResponse> getById(@PathVariable Long id) {
        return Result.success(schoolService.getById(id));
    }

    @GetMapping("/by-major/{majorId}")
    @Operation(summary = "根据专业查询开设院校", description = "根据专业ID查询开设该专业的院校列表，支持分页（按专业名称匹配）")
    public Result<PageResult<SchoolResponse>> getSchoolsByMajor(
            @Parameter(description = "专业ID")
            @PathVariable Long majorId,
            @Parameter(description = "页码（从0开始）")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页条数")
            @RequestParam(defaultValue = "10") int size) {
        return Result.success(schoolService.getSchoolsByMajor(majorId, page, size));
    }

    @GetMapping
    @Operation(summary = "搜索院校", description = "根据关键词、省份、标签等条件分页搜索院校")
    public Result<PageResult<SchoolResponse>> search(
            @Parameter(description = "搜索关键词（匹配院校名称）")
            @RequestParam(required = false) String keyword,
            @Parameter(description = "所在省份")
            @RequestParam(required = false) String province,
            @Parameter(description = "标签（如：985/211/双一流/普通本科/普通专科）")
            @RequestParam(required = false) String tag,
            @Parameter(description = "页码（从0开始）")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页条数")
            @RequestParam(defaultValue = "20") int size) {
        PageResult<SchoolResponse> result = schoolService.search(keyword, province, tag, page, size);
        return Result.success(result);
    }
}
