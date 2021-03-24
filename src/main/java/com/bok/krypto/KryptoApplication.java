package com.bok.krypto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableScheduling
@EnableFeignClients
@EnableDiscoveryClient
@EnableJpaRepositories("com.bok.krypto.repository")
public class KryptoApplication {

    public static void main(String[] args) {
        SpringApplication.run(KryptoApplication.class, args);
    }

}
