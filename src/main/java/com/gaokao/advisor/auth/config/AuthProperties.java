package com.gaokao.advisor.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "auth")
public class AuthProperties {

    private Jwt jwt = new Jwt();
    private Wechat wechat = new Wechat();
    private Sms sms = new Sms();

    @Data
    public static class Jwt {
        private String secret;
        private long expiration = 86400000L;
    }

    @Data
    public static class Wechat {
        private String appId;
        private String appSecret;
        private String loginUrl = "https://api.weixin.qq.com/sns/jscode2session";
    }

    @Data
    public static class Sms {
        private long codeTtl = 300000L;
        private String templateId;
    }
}
