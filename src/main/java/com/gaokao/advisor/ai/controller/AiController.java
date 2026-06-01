package com.gaokao.advisor.ai.controller;

import com.gaokao.advisor.ai.dto.ChatRequest;
import com.gaokao.advisor.ai.dto.ChatResponse;
import com.gaokao.advisor.ai.service.AiService;
import com.gaokao.advisor.common.model.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@Tag(name = "AI 助手", description = "基于智谱GLM-4.5的智能志愿填报咨询助手")
public class AiController {

    private final AiService aiService;

    @PostMapping("/chat")
    @Operation(summary = "AI 对话", description = "向AI助手发送志愿填报相关问题，可附带用户成绩信息获取个性化建议")
    public Result<ChatResponse> chat(@Valid @RequestBody ChatRequest request) {
        return Result.success(aiService.chat(request));
    }
}
