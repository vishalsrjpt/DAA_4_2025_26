package com.daa.jobscheduler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * ============================================================
 * WebConfig — CORS Configuration
 * ============================================================
 * CORS (Cross-Origin Resource Sharing) allows the frontend
 * (running on one origin, e.g. file:// or localhost:5500)
 * to make HTTP requests to the backend (localhost:8080).
 *
 * Without this, browsers block cross-origin requests for security.
 * ============================================================
 */
@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")      // Apply to all /api/* routes
                        .allowedOrigins("*")         // Allow all origins (dev only)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*");
            }
        };
    }
}