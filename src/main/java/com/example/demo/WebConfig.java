// src/main/java/com/example/demo/WebConfig.java
package com.example.demo;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // KYC files
        registry.addResourceHandler("/uploads/kyc/**")
                .addResourceLocations("file:src/main/resources/static/uploads/kyc/");

        // 🆕 Customer photos
        registry.addResourceHandler("/uploads/photos/**")
                .addResourceLocations("file:src/main/resources/static/uploads/photos/");
    }
}
