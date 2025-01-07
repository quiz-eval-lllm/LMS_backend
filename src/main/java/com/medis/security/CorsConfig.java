package com.medis.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://localhost:8080",
                        "http://localhost:8081",
                        "http://104.198.160.93",
                        "http://35.184.2.105",
                        "http://34.69.196.114",
                        "*")
                .allowedMethods("GET", "PUT", "POST", "DELETE", "PATCH");
    }
}
