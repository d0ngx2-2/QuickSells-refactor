package com.example.quicksells.common.filter;

import com.example.quicksells.common.enums.ExceptionCode;
import com.example.quicksells.common.enums.UserRole;
import com.example.quicksells.common.exception.CustomException;
import com.example.quicksells.common.security.JwtAuthenticationToken;
import com.example.quicksells.common.util.JwtUtil;
import com.example.quicksells.domain.auth.model.dto.AuthUser;
import com.example.quicksells.domain.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain) throws ServletException, IOException {

        //  Authorization 헤더 추출
        String authorizationHeader = request.getHeader(JwtUtil.HEADER_KEY);

        // Authorization 헤더 없거나 Bearer 아니면 통과
        if (!jwtUtil.hasAuthorizationHeader(authorizationHeader)) {
            chain.doFilter(request, response);
            return;
        }

        // Bearer 제거
        String token = jwtUtil.substringToken(authorizationHeader);

        // 토큰 검증
        try {
            if (jwtUtil.validateToken(token)) {
                // JWT 복호화
                Claims claims = jwtUtil.extractAllClaims(token);

                // 이미 인증된 요청이 아니라면 SecurityContext에 정보 저장
                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    setAuthentication(claims);
                }
            }
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            log.warn("JWT 인증 실패: {}", e.getMessage());
        }

        chain.doFilter(request, response);
    }

    private void setAuthentication(Claims claims) {

        // JWT에서 사용자 ID 추출
        Long userId = Long.valueOf(claims.getSubject());
        // JWT에서 사용자 email 추출
        String email = claims.get("email", String.class);
        // JWT에서 사용자 이름 추출
        String name = claims.get("name", String.class);
        // JWT에서 사용자 권한 추출
        UserRole userRole = UserRole.of(claims.get("role", String.class));

        userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER));

        AuthUser authUser = new AuthUser(userId, email, userRole, name);

        // Spring Security 인증 객체 생성
        JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(authUser);
        // 요청을 인증된 상태로 저장
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

}

