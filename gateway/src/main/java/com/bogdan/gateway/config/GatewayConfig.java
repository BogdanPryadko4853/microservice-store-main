package com.bogdan.gateway.config;


import com.bogdan.gateway.filter.JwtAuthenticationFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
    private final JwtAuthenticationFilter filter;

    public GatewayConfig(JwtAuthenticationFilter filter) {
        this.filter = filter;
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("order-service", r -> r.path("/api/v1/orders/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://order"))

                .route("customer-service", r -> r.path("/api/v1/customers/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://customer-service"))
                .route("user-service", r -> r.path("/v1/user/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://user-service"))

                .route("auth-service", r -> r.path("/v1/auth/**")
                        .uri("lb://auth-server"))

                .route("payment-service", r -> r.path("/api/v1/payments/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://payment"))
                .route("product-service", r -> r.path("/api/v1/products/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://product"))
                .build();
    }
}