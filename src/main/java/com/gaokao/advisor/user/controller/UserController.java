package com.gaokao.advisor.user.controller;

import com.gaokao.advisor.common.model.Result;
import com.gaokao.advisor.user.dto.UserRequest;
import com.gaokao.advisor.user.dto.UserResponse;
import com.gaokao.advisor.user.dto.UserScoreRequest;
import com.gaokao.advisor.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "用户管理", description = "用户信息管理、注册与资料查询")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "用户名密码注册（非微信/短信注册方式）")
    public Result<UserResponse> register(@Valid @RequestBody UserRequest request) {
        return Result.success(userService.register(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询用户信息", description = "根据用户ID获取用户基本信息")
    public Result<UserResponse> getById(@PathVariable Long id) {
        return Result.success(userService.getById(id));
    }

    @PutMapping("/{id}/scores")
    @Operation(summary = "更新用户成绩", description = "更新用户的高考成绩、排名、考试类型等信息")
    public Result<UserResponse> updateScores(@PathVariable Long id,
                                             @Valid @RequestBody UserScoreRequest request) {
        return Result.success(userService.updateScores(id, request));
    }
}
