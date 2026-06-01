package com.gaokao.advisor.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "更新用户成绩请求")
public class UserScoreRequest {

    @Schema(description = "高考总分", example = "600")
    private Integer totalScore;

    @Schema(description = "全省排名", example = "15000")
    private Integer rankNum;

    @Schema(description = "考试类型（物理类/历史类）", example = "物理类")
    private String examType;

    @Schema(description = "所在省份", example = "河南")
    private String province;

    @Schema(description = "毕业年份", example = "2025")
    private Integer graduationYear;
}
