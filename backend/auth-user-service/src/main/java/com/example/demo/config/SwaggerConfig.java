package com.example.demo.config;
import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.*;
import io.swagger.v3.oas.models.security.*;
import org.springframework.context.annotation.*;
@Configuration public class SwaggerConfig {
    @Bean OpenAPI api() {
        return new OpenAPI().info(new Info().title("Auth User Service").version("1.0")).components(new Components().addSecuritySchemes("bearerAuth",new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
    }
}
