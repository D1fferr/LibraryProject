package com.Library.UserService.services;

import com.Library.UserService.util.FailedToConnectWithRedisException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenBlackListService {


    private final StringRedisTemplate redisTemplate;
    private final long blacklistTtl = 7200;

    @CacheEvict(value = "tokenCheckCache", key = "#token")
    public void blacklistToken(String token) {
        String blacklistKey = "blacklist:" + token;
        try {
            log.info("Trying to add the password to the redis black list");
            redisTemplate.opsForValue().set(blacklistKey, "blacklisted", blacklistTtl, TimeUnit.SECONDS);
            log.info("Added the password to the redis black list");
        }catch (Exception e){
            log.warn("Failed to add the password to the redis black list");
            throw new FailedToConnectWithRedisException(e.getMessage());
        }
    }

//    @Cacheable(value = "tokenCheckCache", key = "#token", unless = "#result == true")
//    public boolean isTokenBlacklisted(String token) {
//        String blacklistKey = "blacklist:" + token;
//        return Boolean.TRUE.equals(redisTemplate.hasKey(blacklistKey));
//    }
}