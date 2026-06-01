package com.gaokao.advisor.major.controller;

import com.gaokao.advisor.common.model.PageResult;
import com.gaokao.advisor.common.model.Result;
import com.gaokao.advisor.major.dto.MajorRequest;
import com.gaokao.advisor.major.dto.MajorResponse;
import com.gaokao.advisor.major.service.MajorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/majors")
@RequiredArgsConstructor
@Tag(name = "专业管理", description = "高等院校专业信息查询，支持按名称模糊搜索和学科门类筛选")
public class MajorController {

    private final MajorService majorService;

    @PostMapping
    @Operation(summary = "新增专业", description = "添加一个新专业（管理端接口）")
    public Result<MajorResponse> create(@Valid @RequestBody MajorRequest request) {
        return Result.success(majorService.create(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询专业详情", description = "根据专业ID获取详细信息")
    public Result<MajorResponse> getById(@PathVariable Long id) {
        return Result.success(majorService.getById(id));
    }

    @GetMapping
    @Operation(summary = "搜索专业", description = "根据关键词、学科门类等条件分页搜索专业，或查询某院校下的全部专业")
    public Result<?> list(
            @Parameter(description = "所属院校ID（指定后查询该校全部专业）")
            @RequestParam(required = false) Long schoolId,
            @Parameter(description = "搜索关键词（匹配专业名称）")
            @RequestParam(required = false) String keyword,
            @Parameter(description = "学科门类（如：工学、理学、医学）")
            @RequestParam(required = false) String category,
            @Parameter(description = "页码（从0开始）")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页条数")
            @RequestParam(defaultValue = "20") int size) {
        if (schoolId != null) {
            return Result.success(majorService.listBySchool(schoolId));
        }
        return Result.success(majorService.search(keyword, category, page, size));
    }
}
