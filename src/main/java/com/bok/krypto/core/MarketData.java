package com.bok.krypto.core;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MarketData {

    @Scheduled(fixedRate = 300000)
    public void fetchData(){

    }
}
