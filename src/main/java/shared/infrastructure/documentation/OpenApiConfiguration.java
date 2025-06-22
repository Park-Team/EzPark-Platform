package com.acme.ezpark.platform.shared.infrastructure.documentation;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI ezParkPlatformOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("EzPark Platform API")
                        .description("P2P Parking Solution Platform - Backend API for managing users, vehicles, parking spaces, bookings, payments, schedules, and locations")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("EzPark Platform Team")
                                .email("support@ezpark.com")
                                .url("https://www.ezpark.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .externalDocs(new ExternalDocumentation()
                        .description("EzPark Platform Documentation")
                        .url("https://docs.ezpark.com"));
    }
}
