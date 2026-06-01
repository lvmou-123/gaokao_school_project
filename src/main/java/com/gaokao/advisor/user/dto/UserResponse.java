package com.gaokao.advisor.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "用户信息响应")
public class UserResponse {

    @Schema(description = "用户ID", example = "1")
    private Long id;

    @Schema(description = "用户名", example = "zhangsan")
    private String username;

    @Schema(description = "真实姓名", example = "张三")
    private String realName;

    @Schema(description = "手机号", example = "13800138000")
    private String phone;

    @Schema(description = "所在省份", example = "河南")
    private String province;

    @Schema(description = "考试类型（物理类/历史类）", example = "物理类")
    private String examType;

    @Schema(description = "高考总分", example = "600")
    private Integer totalScore;

    @Schema(description = "全省排名", example = "15000")
    private Integer rankNum;
}
