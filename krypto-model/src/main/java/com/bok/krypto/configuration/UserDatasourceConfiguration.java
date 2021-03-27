package com.bok.krypto.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;


@Configuration
public class UserDatasourceConfiguration {
    @Bean
    @ConfigurationProperties(prefix = "spring.user-datasource")
    public DataSource secondaryDataSource() {
        return DataSourceBuilder.create().build();
    }
}
