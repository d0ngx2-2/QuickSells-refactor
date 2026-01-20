package com.example.quicksells.common.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfig {
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager manager = new CaffeineCacheManager("searchResult");

        manager.setCaffeine(
                Caffeine.newBuilder()
                        .maximumSize(5000) //최대 캐시 개수
                        .expireAfterWrite(Duration.ofDays(1)) //1일 TTL
        );
        return manager;
    }
}
