package com.gaokao.advisor.ai.service.impl;

import com.gaokao.advisor.ai.config.AiProperties;
import com.gaokao.advisor.ai.dto.ChatRequest;
import com.gaokao.advisor.ai.dto.ChatResponse;
import com.gaokao.advisor.ai.service.AiService;
import com.gaokao.advisor.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiServiceImpl implements AiService {

    private final RestTemplate restTemplate;
    private final AiProperties aiProperties;

    @Override
    public ChatResponse chat(ChatRequest request) {
        String url = aiProperties.getZhipu().getBaseUrl() + "/chat/completions";
        String apiKey = aiProperties.getZhipu().getApiKey();
        String model = aiProperties.getZhipu().getModel();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        List<Map<String, String>> messages = buildMessages(request);

        Map<String, Object> body = Map.of(
                "model", model,
                "messages", messages,
                "stream", false,
                "max_tokens", 2048,
                "temperature", 0.7
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.postForEntity(url, entity, (Class<Map<String, Object>>) (Class<?>) Map.class);
            Map<String, Object> responseBody = response.getBody();

            if (responseBody == null || !responseBody.containsKey("choices")) {
                log.error("ZhipuAI response invalid: {}", responseBody);
                throw new BusinessException("AI 服务响应异常");
            }

            List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
            if (choices.isEmpty()) {
                throw new BusinessException("AI 服务未返回结果");
            }

            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
            String reply = (String) message.get("content");

            return ChatResponse.builder()
                    .reply(reply)
                    .model(model)
                    .build();

        } catch (RestClientException e) {
            log.error("ZhipuAI API call failed", e);
            throw new BusinessException("AI 服务暂时不可用，请稍后重试");
        }
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, String>> buildMessages(ChatRequest request) {
        StringBuilder prompt = new StringBuilder();
        prompt.append(request.getMessage());

        List<String> context = new ArrayList<>();
        if (request.getScore() != null) {
            context.add("高考分数: " + request.getScore());
        }
        if (request.getRankNum() != null) {
            context.add("全省排名: " + request.getRankNum());
        }
        if (request.getProvince() != null) {
            context.add("所在省份: " + request.getProvince());
        }

        if (!context.isEmpty()) {
            prompt.append("\n\n我的基本情况: ").append(String.join(", ", context));
        }

        return List.of(
                Map.of("role", "system", "content", aiProperties.getZhipu().getSystemPrompt()),
                Map.of("role", "user", "content", prompt.toString())
        );
    }
}
