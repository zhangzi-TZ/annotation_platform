package com.annotationplatform.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final StorageProperties storageProperties;

    public WebConfig(StorageProperties storageProperties) {
        this.storageProperties = storageProperties;
    }

    // Media files are now served by MediaController for better control over headers
    // No need for static resource handler

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Get allowed origins from environment variable, default to localhost for development
        String allowedOrigins = System.getenv().getOrDefault("CORS_ALLOWED_ORIGINS", 
            "http://localhost:5173,https://*.netlify.app");
        
        // CORS for API endpoints
        registry.addMapping("/api/**")
                .allowedOriginPatterns(allowedOrigins.split(","))
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD")
                .allowedHeaders("*")
                .allowCredentials(true);
        
        // CORS for media files (videos)
        registry.addMapping("/media/**")
                .allowedOriginPatterns(allowedOrigins.split(","))
                .allowedMethods("GET", "OPTIONS", "HEAD")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
