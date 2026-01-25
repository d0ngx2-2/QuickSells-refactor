package com.example.quicksells.common.redis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenBlackListServiceImpl implements TokenBlackListService {

    private final RedisTemplate<String, Object> redisTemplate;

    private final String REDIS_BLACKLIST_PREFIX = "tokenBlackList";

    /**
     * 토큰을 블랙리스트에 추가 (TTL 적용)
     *
     * @param token          차단할 토큰
     * @param expirationTime 토큰의 남은 유효 시간
     */
    public void addTokenToBlacklist(String token, Long expirationTime) {

        redisTemplate.opsForValue().set(
                REDIS_BLACKLIST_PREFIX + token,
                "logout",
                expirationTime,
                TimeUnit.MILLISECONDS
        );
    }

    /**
     * 블랙리스트 포함 여부 확인
     */
    @Override
    public boolean isContainToken(String token) {

        return Boolean.TRUE.equals(redisTemplate.hasKey(REDIS_BLACKLIST_PREFIX + token));
    }

}
