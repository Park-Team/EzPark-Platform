package com.acme.ezpark.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class EzParkPlatformApplication {

    public static void main(String[] args) {
        System.out.println("Starting EzPark Platform Application...");
        SpringApplication.run(EzParkPlatformApplication.class, args);
    }
    
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        System.out.println("EzPark Platform Application is ready!");
        System.out.println("Swagger UI available at: /swagger-ui/index.html");
        System.out.println("API Documentation available at: /api-docs");
    }
}
