package com.gaokao.advisor.ai.service;

import com.gaokao.advisor.ai.dto.ChatRequest;
import com.gaokao.advisor.ai.dto.ChatResponse;

public interface AiService {

    ChatResponse chat(ChatRequest request);
}
