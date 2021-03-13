package com.bok.krypto.cache;

import com.bok.krypto.core.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CacheService {

    @CacheEvict(value = Constants.PRICES, allEntries = true)
    public void evictPrices() {
        log.info("Evicted prices cache");
    }

    @CacheEvict(value = Constants.KRYPTO, allEntries = true)
    public void evictKryptos() {
        log.info("Evicted kryptos cache");
    }
}
