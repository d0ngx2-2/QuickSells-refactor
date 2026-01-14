package com.example.quicksells.common.util;


import com.example.quicksells.common.enums.ExceptionCode;
import com.example.quicksells.common.enums.UserRole;
import com.example.quicksells.common.exception.CustomException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j(topic = "JwtUtil")
@Component
public class JwtUtil {

    public static final String HEADER_KEY = "Authorization"; // JWT 토큰이 담기는 HTTP 헤더 키
    public static final String BEARER_PREFIX = "Bearer ";   // 헤더 BearerToken
    private static final long TOKEN_TIME = 60 * 60 * 1000L; // 토큰 유효시간(60분)

    @Value("${jwt.secret.key}")
    private String secretKeyString;

    private SecretKey key;
    private JwtParser parser;

    // @PostConstruct 어플리케이션 실행 될 때 가장 먼저 실행 되게 하는 어노테이션
    // JWT 서명 검증을 위한 Key 준비 + parser 준비 (빈 초기화 메서드)
    @PostConstruct
    public void init() {
        byte[] bytes = Decoders.BASE64.decode(secretKeyString);      //  secret key → byte[]로 변환
        this.key = Keys.hmacShaKeyFor(bytes);                        // JWT 서명을 검증할 Key 객체 생성
        this.parser = Jwts.parser()                                  // 나중에 JWT 토큰을 파싱·검증할 parser 생성
                .verifyWith(this.key)
                .build();
    }

    // 토큰 생성
    public String generateToken(Long userId, String email, String name, UserRole role) {
        Date now = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .subject(userId.toString())
                        .claim("email", email)
                        .claim("name", name)
                        .claim("role", role.name())
                        .issuedAt(now) // 발급일
                        .expiration(new Date(now.getTime() + TOKEN_TIME))
                        .signWith(key, Jwts.SIG.HS256) // 암호화 알고리즘
                        .compact();
    }

    // bearer 체크
    public boolean hasAuthorizationHeader(String header) {return header != null && header.startsWith(BEARER_PREFIX);}

    // 토큰 헤더 분리
    public String substringToken(String tokenValue) {
        if (hasAuthorizationHeader(tokenValue)) {
            return tokenValue.substring(7);
        }
        throw new CustomException(ExceptionCode.NOT_FOUND_TOKEN);
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        if (token == null || token.isEmpty()) return false;

        try {
            parser.parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.debug("Invalid JWT: {}", e.toString());
            return false;
        }
    }

    // 토큰 복호화
    public Claims extractAllClaims(String token) {return parser.parseSignedClaims(token).getPayload();}
}

