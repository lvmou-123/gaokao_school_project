package com.gaokao.advisor.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI gaokaoAdvisorOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("高考志愿填报助手 API")
                        .description("高考志愿填报小程序后端接口文档，支持院校查询、专业查询、AI 智能推荐等功能")
                        .version("1.0.0")
                        .contact(new Contact().name("开发团队")))
                .components(new Components()
                        .addSecuritySchemes("bearer-jwt", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT Token，在登录/注册接口获取")))
                .security(List.of(new SecurityRequirement().addList("bearer-jwt")));
    }
}
