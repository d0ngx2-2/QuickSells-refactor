package com.example.quicksells.common.redis.service;

import org.springframework.stereotype.Service;

@Service
public interface TokenBlackListService {

    // 블랙리스트에 추가 (TTL 포함)
    void addTokenToBlacklist(String token, Long expirationTime);

    // 블랙리스트 포함 여부 확인 (로그인 차단 체크용)
    boolean isContainToken(String token);
}
