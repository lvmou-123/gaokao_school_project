package com.gaokao.advisor.auth.service.impl;

import com.gaokao.advisor.auth.dto.*;
import com.gaokao.advisor.auth.service.AuthService;
import com.gaokao.advisor.auth.utils.JwtUtils;
import com.gaokao.advisor.auth.utils.SmsCodeManager;
import com.gaokao.advisor.auth.utils.WeChatUtils;
import com.gaokao.advisor.common.exception.BusinessException;
import com.gaokao.advisor.common.exception.ErrorCode;
import com.gaokao.advisor.user.entity.User;
import com.gaokao.advisor.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final SmsCodeManager smsCodeManager;
    private final WeChatUtils weChatUtils;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void sendSms(SendSmsRequest request) {
        String code = smsCodeManager.generateAndStore(request.getPhone());
        log.info("SMS verification code for {}: {}", request.getPhone(), code);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AuthResponse register(RegisterRequest request) {
        smsCodeManager.verify(request.getPhone(), request.getCode());

        if (userRepository.existsByPhone(request.getPhone())) {
            throw new BusinessException(ErrorCode.CONFLICT, "该手机号已注册");
        }

        User user = new User();
        user.setPhone(request.getPhone());
        user.setUsername(request.getPhone());
        user.setRealName(request.getRealName());
        if (request.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        userRepository.save(user);
        log.info("User registered: phone={}", request.getPhone());

        String token = jwtUtils.generateToken(user.getId());
        return AuthResponse.builder()
                .token(token)
                .userId(user.getId())
                .phone(user.getPhone())
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AuthResponse loginByPhone(PhoneLoginRequest request) {
        smsCodeManager.verify(request.getPhone(), request.getCode());

        User user = userRepository.findByPhone(request.getPhone())
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setPhone(request.getPhone());
                    newUser.setUsername(request.getPhone());
                    return userRepository.save(newUser);
                });

        String token = jwtUtils.generateToken(user.getId());
        log.info("User logged in via phone: userId={}", user.getId());

        return AuthResponse.builder()
                .token(token)
                .userId(user.getId())
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .phone(user.getPhone())
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AuthResponse loginByWeChat(WeChatLoginRequest request) {
        Map<String, Object> session = weChatUtils.exchangeCodeForSession(request.getCode());
        String openId = (String) session.get("openid");

        User user = userRepository.findByOpenId(openId)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setOpenId(openId);
                    newUser.setNickname(request.getNickname());
                    newUser.setAvatar(request.getAvatar());
                    return userRepository.save(newUser);
                });

        if (request.getNickname() != null) {
            user.setNickname(request.getNickname());
        }
        if (request.getAvatar() != null) {
            user.setAvatar(request.getAvatar());
        }

        String token = jwtUtils.generateToken(user.getId());
        log.info("User logged in via WeChat: userId={}, openId={}", user.getId(), openId);

        return AuthResponse.builder()
                .token(token)
                .userId(user.getId())
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .phone(user.getPhone())
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AuthResponse loginByWeChatPhone(WeChatPhoneLoginRequest request) {
        Map<String, Object> session = weChatUtils.exchangeCodeForSession(request.getCode());
        String openId = (String) session.get("openid");
        String sessionKey = (String) session.get("session_key");

        String phoneNumber = weChatUtils.decryptPhoneNumber(sessionKey, request.getEncryptedData(), request.getIv());

        User user = userRepository.findByPhone(phoneNumber)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setPhone(phoneNumber);
                    newUser.setUsername(phoneNumber);
                    newUser.setOpenId(openId);
                    return userRepository.save(newUser);
                });

        if (user.getOpenId() == null) {
            user.setOpenId(openId);
        }

        String token = jwtUtils.generateToken(user.getId());
        log.info("User logged in via WeChat phone: userId={}, phone={}", user.getId(), phoneNumber);

        return AuthResponse.builder()
                .token(token)
                .userId(user.getId())
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .phone(user.getPhone())
                .build();
    }

    @SuppressWarnings("unchecked")
    private String exchangeCodeForOpenId(String code) {
        Map<String, Object> session = weChatUtils.exchangeCodeForSession(code);
        return (String) session.get("openid");
}

}
