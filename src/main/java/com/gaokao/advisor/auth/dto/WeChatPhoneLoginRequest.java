package com.gaokao.advisor.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "微信小程序手机号一键登录请求")
public class WeChatPhoneLoginRequest {

    @NotBlank(message = "微信登录凭证不能为空")
    @Schema(description = "微信code（wx.login获取）", example = "071ZkB0w3iQ52O2hPx2w3qEo1q3ZkB0u")
    private String code;

    @NotBlank(message = "加密数据不能为空")
    @Schema(description = "encryptedData（getPhoneNumber回调获取）", example = "CiyLU1B9D2Vq...")
    private String encryptedData;

    @NotBlank(message = "加密向量不能为空")
    @Schema(description = "iv（getPhoneNumber回调获取）", example = "r7BXI2rRfR1dP1aP...")
    private String iv;
}
