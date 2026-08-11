package com.example.cursos.gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRoutesConfig {

    private static final String CORS_HEADERS =
            "Access-Control-Allow-Credentials Access-Control-Allow-Origin";

    @Bean
    RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                // Debe ir antes de /api/courses/** para que las reseñas pertenezcan a Engagement.
                .route("engagement-public-reviews", route -> route
                        .path("/api/courses/*/reviews")
                        .filters(f -> f.dedupeResponseHeader(CORS_HEADERS, "RETAIN_FIRST"))
                        .uri("lb://engagement-service"))

                .route("auth-user", route -> route
                        .path("/api/auth/**", "/api/account/**", "/api/admin/users/**", "/api/admin/security-check")
                        .filters(f -> f.dedupeResponseHeader(CORS_HEADERS, "RETAIN_FIRST"))
                        .uri("lb://auth-user-service"))

                .route("course", route -> route
                        .path("/api/courses/**", "/api/admin/courses/**")
                        .filters(f -> f.dedupeResponseHeader(CORS_HEADERS, "RETAIN_FIRST"))
                        .uri("lb://course-service"))

                .route("enrollment", route -> route
                        .path("/api/enrollments/**")
                        .filters(f -> f.dedupeResponseHeader(CORS_HEADERS, "RETAIN_FIRST"))
                        .uri("lb://enrollment-service"))

                .route("engagement", route -> route
                        .path("/api/notifications/**", "/api/favorites/**", "/api/reviews/**")
                        .filters(f -> f.dedupeResponseHeader(CORS_HEADERS, "RETAIN_FIRST"))
                        .uri("lb://engagement-service"))

                .route("operations", route -> route
                        .path("/api/admin/reports/**", "/api/admin/audit/**", "/api/public/health")
                        .filters(f -> f.dedupeResponseHeader(CORS_HEADERS, "RETAIN_FIRST"))
                        .uri("lb://admin-operations-service"))
                .build();
    }
}
