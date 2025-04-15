package org.daylight.routenavigator.backend.utility;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNullApi;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {
    @Bean
    public CacheManager cacheManager() {
        return new CaffeineCacheManager() {
            @Override
            protected Cache<Object, Object> createNativeCaffeineCache(String name) {
                Caffeine<Object, Object> builder = Caffeine.newBuilder()
                        .maximumSize(100)
                        .expireAfterWrite(getTtlForCache(name), TimeUnit.SECONDS);
                return builder.build();
            }
        };
    }

    private long getTtlForCache(String cacheName) {
        switch (cacheName) {
            case "routes":
                return 30;
            case "routeDates":
                return 60;
            default:
                return 30;
        }
    }
}