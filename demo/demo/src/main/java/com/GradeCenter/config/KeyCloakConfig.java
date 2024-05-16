package com.GradeCenter.config;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class KeyCloakConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
