package com.example.iremboback.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class PublicAPIConfiguration {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}