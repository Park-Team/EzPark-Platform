package com.acme.ezpark.platform.shared.infrastructure.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    @PostConstruct
    public void init() {
        try {
            // Create directories if they don't exist
            Path uploadPath = Paths.get(uploadDir);
            Path profilePicturesPath = uploadPath.resolve("profile-pictures");
            Path parkingImagesPath = uploadPath.resolve("parking-images");
            Path generalPath = uploadPath.resolve("general");
            
            Files.createDirectories(profilePicturesPath);
            Files.createDirectories(parkingImagesPath);
            Files.createDirectories(generalPath);
            
        } catch (Exception e) {
            System.err.println("Error initializing upload directories: " + e.getMessage());
        }
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        try {
            // Convert relative path to absolute path
            File uploadDirFile = new File(uploadDir);
            String absoluteUploadDir = uploadDirFile.getAbsolutePath();
            
            // Serve uploaded files
            registry.addResourceHandler("/profile-pictures/**")
                    .addResourceLocations("file:" + absoluteUploadDir + "/profile-pictures/")
                    .setCachePeriod(3600)
                    .resourceChain(true);
            
            registry.addResourceHandler("/parking-images/**")
                    .addResourceLocations("file:" + absoluteUploadDir + "/parking-images/")
                    .setCachePeriod(3600)
                    .resourceChain(true);
            
            registry.addResourceHandler("/general/**")
                    .addResourceLocations("file:" + absoluteUploadDir + "/general/")
                    .setCachePeriod(3600)
                    .resourceChain(true);
            
        } catch (Exception e) {
            System.err.println("Error configuring static resource handlers: " + e.getMessage());
        }
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Add CORS configuration for static resources
        registry.addMapping("/profile-pictures/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "HEAD", "OPTIONS")
                .allowedHeaders("*")
                .maxAge(3600);
                
        registry.addMapping("/parking-images/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "HEAD", "OPTIONS")
                .allowedHeaders("*")
                .maxAge(3600);
                
        registry.addMapping("/general/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "HEAD", "OPTIONS")
                .allowedHeaders("*")
                .maxAge(3600);
    }
}
