package com.Library.UserService.services;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TokenBlackListService {


    private final RedisTemplate<String, String> redisTemplate;
    private final long blacklistTtl = 7200;

    public TokenBlackListService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @CacheEvict(value = "tokenCheckCache", key = "#token")
    public void blacklistToken(String token) {
        String blacklistKey = "blacklist:" + token;
        redisTemplate.opsForValue().set(blacklistKey, "blacklisted", blacklistTtl, TimeUnit.SECONDS);

    }

    @Cacheable(value = "tokenCheckCache", key = "#token", unless = "#result == true")
    public boolean isTokenBlacklisted(String token) {
        String blacklistKey = "blacklist:" + token;
        return Boolean.TRUE.equals(redisTemplate.hasKey(blacklistKey));
    }
}