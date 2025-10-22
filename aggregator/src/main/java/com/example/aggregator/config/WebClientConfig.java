package com.example.aggregator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient customerClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:6060")
                .build();
    }

    @Bean
    public WebClient stockClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:7070")
                .build();
    }
}
