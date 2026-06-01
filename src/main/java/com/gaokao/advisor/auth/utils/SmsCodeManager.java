package com.gaokao.advisor.auth.utils;

import com.gaokao.advisor.auth.config.AuthProperties;
import com.gaokao.advisor.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class SmsCodeManager {

    private final StringRedisTemplate redisTemplate;
    private final AuthProperties authProperties;

    private static final String KEY_PREFIX = "sms:code:";

    public String generateAndStore(String phone) {
        String code = "123456";
        String key = KEY_PREFIX + phone;
        long ttlMs = authProperties.getSms().getCodeTtl();
        redisTemplate.opsForValue().set(key, code, ttlMs, TimeUnit.MILLISECONDS);
        log.info("SMS code for {}: {}", phone, code);
        return code;
    }

    public void verify(String phone, String code) {
        String key = KEY_PREFIX + phone;
        String stored = redisTemplate.opsForValue().get(key);
        if (stored == null) {
            throw new BusinessException("验证码未发送或已过期");
        }
        if (!stored.equals(code)) {
            throw new BusinessException("验证码错误");
        }
        redisTemplate.delete(key);
    }
}
