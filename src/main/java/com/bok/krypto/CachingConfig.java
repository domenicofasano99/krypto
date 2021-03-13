package com.bok.krypto;

import com.bok.krypto.core.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
@Slf4j
public class CachingConfig {

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(Constants.PRICES);
    }

    //@Scheduled()
    @CacheEvict(value = Constants.PRICES, allEntries = true)
    public void cacheRefresh() {
        log.info("Evicted prices cache");
    }
}