package com.gaokao.advisor.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "用户注册请求（用户名密码方式）")
public class UserRequest {

    @NotBlank(message = "用户名不能为空")
    @Schema(description = "用户名", example = "zhangsan")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Schema(description = "密码", example = "mypassword123")
    private String password;

    @Schema(description = "真实姓名", example = "张三")
    private String realName;

    @Schema(description = "手机号", example = "13800138000")
    private String phone;

    @Schema(description = "所在省份", example = "河南")
    private String province;

    @Schema(description = "考试类型（物理类/历史类）", example = "物理类")
    private String examType;

    @Schema(description = "毕业年份", example = "2025")
    private Integer graduationYear;

    @Schema(description = "高考总分", example = "600")
    private Integer totalScore;

    @Schema(description = "全省排名", example = "15000")
    private Integer rankNum;
}
