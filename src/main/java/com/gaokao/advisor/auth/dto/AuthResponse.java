package com.gaokao.advisor.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "认证响应（JWT令牌 + 用户基本信息）")
public class AuthResponse {

    @Schema(description = "JWT访问令牌", example = "eyJhbGciOiJIUzI1NiJ9.xxx")
    private String token;

    @Schema(description = "用户ID", example = "1")
    private Long userId;

    @Schema(description = "用户昵称", example = "小明")
    private String nickname;

    @Schema(description = "头像URL", example = "https://example.com/avatar.png")
    private String avatar;

    @Schema(description = "手机号", example = "13800138000")
    private String phone;
}
