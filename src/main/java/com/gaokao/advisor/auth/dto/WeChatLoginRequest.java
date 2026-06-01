package com.gaokao.advisor.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "微信小程序登录请求")
public class WeChatLoginRequest {

    @NotBlank(message = "微信登录凭证不能为空")
    @Schema(description = "微信code（临时登录凭证）", example = "071ZkB0w3iQ52O2hPx2w3qEo1q3ZkB0u")
    private String code;

    @Schema(description = "微信用户昵称", example = "小明")
    private String nickname;

    @Schema(description = "微信头像URL", example = "https://thirdwx.qlogo.cn/xxx")
    private String avatar;
}
