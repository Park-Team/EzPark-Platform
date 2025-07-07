package com.acme.ezpark.platform.shared.infrastructure.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve uploaded files
        registry.addResourceHandler("/profile-pictures/**")
                .addResourceLocations("file:" + uploadDir + "/profile-pictures/");
        
        registry.addResourceHandler("/parking-images/**")
                .addResourceLocations("file:" + uploadDir + "/parking-images/");
        
        registry.addResourceHandler("/general/**")
                .addResourceLocations("file:" + uploadDir + "/general/");
    }
}
