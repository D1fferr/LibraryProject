package library.com.apigateway.security;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class JwtBlacklistHandler {

    private final ReactiveStringRedisTemplate redisTemplate;
    private final Cache<String, Boolean> localCache;

    private static final String BLACKLIST_PREFIX = "blacklist:";

    public JwtBlacklistHandler(ReactiveStringRedisTemplate redisTemplate, Cache<String, Boolean> localCache) {
        this.redisTemplate = redisTemplate;
        this.localCache = localCache;
    }

    public Mono<Boolean> isTokenBlacklisted(String token) {
        String fullKey = BLACKLIST_PREFIX + token;

        Boolean isBlacklisted = localCache.getIfPresent(fullKey);
        if (isBlacklisted != null) {
            return Mono.just(isBlacklisted);
        }

        return redisTemplate.hasKey(fullKey)
                .map(exists -> {
                    localCache.put(fullKey, exists);
                    return exists;
                })
                .defaultIfEmpty(false);
    }
}
