package com.gaokao.advisor.application.controller;

import com.gaokao.advisor.application.dto.ApplicationRequest;
import com.gaokao.advisor.application.dto.ApplicationResponse;
import com.gaokao.advisor.application.service.ApplicationService;
import com.gaokao.advisor.common.model.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
@Tag(name = "志愿管理", description = "用户志愿表管理，支持创建、查询和修改志愿状态")
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping
    @Operation(summary = "添加志愿", description = "为用户添加一条志愿记录（含院校、专业和优先级）")
    public Result<ApplicationResponse> create(@Valid @RequestBody ApplicationRequest request) {
        return Result.success(applicationService.create(request));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "查询用户志愿列表", description = "查询指定用户的所有志愿记录，按优先级排序")
    public Result<List<ApplicationResponse>> listByUser(@PathVariable Long userId) {
        return Result.success(applicationService.listByUser(userId));
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "更新志愿状态", description = "更新指定志愿的状态（draft / submitted / admitted）")
    public Result<ApplicationResponse> updateStatus(
            @PathVariable Long id,
            @Parameter(description = "新状态：draft（草稿）、submitted（已提交）、admitted（已录取）")
            @RequestParam String status) {
        return Result.success(applicationService.updateStatus(id, status));
    }
}
