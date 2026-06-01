package com.gaokao.advisor.auth.service;

import com.gaokao.advisor.auth.dto.*;

public interface AuthService {

    void sendSms(SendSmsRequest request);

    AuthResponse register(RegisterRequest request);

    AuthResponse loginByPhone(PhoneLoginRequest request);

    AuthResponse loginByWeChat(WeChatLoginRequest request);

    AuthResponse loginByWeChatPhone(WeChatPhoneLoginRequest request);
}
