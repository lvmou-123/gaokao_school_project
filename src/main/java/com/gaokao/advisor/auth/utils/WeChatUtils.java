package com.gaokao.advisor.auth.utils;

import com.gaokao.advisor.auth.config.AuthProperties;
import com.gaokao.advisor.common.exception.BusinessException;
import com.gaokao.advisor.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class WeChatUtils {

    private final AuthProperties authProperties;
    private final RestTemplate restTemplate;

    private static final String SESSION_URL = "https://api.weixin.qq.com/sns/jscode2session";

    @SuppressWarnings("unchecked")
    public Map<String, Object> exchangeCodeForSession(String code) {
        AuthProperties.Wechat wechat = authProperties.getWechat();
        String url = String.format("%s?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                SESSION_URL, wechat.getAppId(), wechat.getAppSecret(), code);

        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            if (response == null) {
                throw new BusinessException("微信登录失败");
            }
            if (response.containsKey("errcode")) {
                int errCode = ((Number) response.get("errcode")).intValue();
                String errMsg = (String) response.getOrDefault("errmsg", "未知错误");
                throw new BusinessException(ErrorCode.BAD_REQUEST, "微信登录失败: " + errMsg);
            }
            return response;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("WeChat API call failed", e);
            throw new BusinessException("微信登录服务异常");
        }
    }

    public String decryptPhoneNumber(String sessionKey, String encryptedData, String iv) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(sessionKey);
            byte[] ivBytes = Base64.getDecoder().decode(iv);
            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);

            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
            AlgorithmParameterSpec paramSpec = new IvParameterSpec(ivBytes);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, paramSpec);

            byte[] decrypted = cipher.doFinal(encryptedBytes);
            String result = new String(decrypted, StandardCharsets.UTF_8);

            Map<?, ?> data = com.fasterxml.jackson.databind.json.JsonMapper.builder()
                    .build().readValue(result, Map.class);

            String phoneNumber = (String) data.get("purePhoneNumber");
            if (phoneNumber == null) {
                throw new BusinessException("解密失败，未获取到手机号");
            }
            return phoneNumber;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("WeChat phone decrypt failed", e);
            throw new BusinessException("手机号解密失败");
        }
    }
}
