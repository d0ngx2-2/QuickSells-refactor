package com.example.quicksells.common.handler;

import com.example.quicksells.common.redis.service.TokenBlackListService;
import com.example.quicksells.common.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
public class CustomLogoutHandler implements LogoutHandler {

    @Autowired
    private TokenBlackListService tokenBlackListService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        String headerToken = request.getHeader(JwtUtil.HEADER_KEY);

        // 헤더 검증 및 Bearer 접두사 제거
        if (jwtUtil.hasAuthorizationHeader(headerToken)) {
            String token = jwtUtil.substringToken(headerToken);

            // 토큰 유효성 검증
            if (jwtUtil.validateToken(token)) {
                // 남은 유효 시간 계산
                long remainingTime = jwtUtil.getRemainingTime(token);

                // Redis 블랙리스트에 추가
                if (remainingTime > 0) {
                    tokenBlackListService.addTokenToBlacklist(token, remainingTime);
                }
            }
        }
    }
}
