package com.gaokao.advisor.auth.controller;

import com.gaokao.advisor.auth.dto.*;
import com.gaokao.advisor.auth.service.AuthService;
import com.gaokao.advisor.common.model.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "认证管理", description = "用户注册、登录（手机验证码 + 微信小程序）")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sms")
    @Operation(summary = "发送短信验证码", description = "向指定手机号发送6位数字验证码，验证码有效期为5分钟")
    public Result<Void> sendSms(@Valid @RequestBody SendSmsRequest request) {
        authService.sendSms(request);
        return Result.success();
    }

    @PostMapping("/register")
    @Operation(summary = "手机号注册", description = "通过手机号+验证码注册新用户，自动返回JWT令牌")
    public Result<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return Result.success(authService.register(request));
    }

    @PostMapping("/login/phone")
    @Operation(summary = "手机号登录", description = "通过手机号+验证码快速登录，新用户自动注册")
    public Result<AuthResponse> loginByPhone(@Valid @RequestBody PhoneLoginRequest request) {
        return Result.success(authService.loginByPhone(request));
    }

    @PostMapping("/login/wechat")
    @Operation(summary = "微信小程序登录", description = "通过微信code换取openId，新用户自动注册，返回JWT令牌")
    public Result<AuthResponse> loginByWeChat(@Valid @RequestBody WeChatLoginRequest request) {
        return Result.success(authService.loginByWeChat(request));
    }

    @PostMapping("/login/wechat-phone")
    @Operation(summary = "微信小程序手机号一键登录", description = "通过encryptedData解密获取手机号，新用户自动注册，返回JWT令牌")
    public Result<AuthResponse> loginByWeChatPhone(@Valid @RequestBody WeChatPhoneLoginRequest request) {
        return Result.success(authService.loginByWeChatPhone(request));
    }
}
