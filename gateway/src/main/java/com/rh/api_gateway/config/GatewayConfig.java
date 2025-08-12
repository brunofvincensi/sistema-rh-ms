package com.rh.api_gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;

import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.time.Duration;
import java.util.Arrays;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service", r -> r
                        .path("/api/user/**")
                        .filters(f -> f.stripPrefix(2))
                        .uri("http://localhost:8081"))

                .route("employee-service", r -> r
                        .path("/api/employee/**")
                        .filters(f -> f.stripPrefix(2))
                        .uri("http://localhost:8082"))

                .route("time-entry-service", r -> r
                        .path("/api/time-entry/**")
                        .filters(f -> f.stripPrefix(2))
                        .uri("http://localhost:8083"))

                .route("payroll-service", r -> r
                        .path("/api/payroll/**")
                        .filters(f -> f.stripPrefix(2))
                        .uri("http://localhost:8084"))

                .route("email-service", r -> r
                        .path("/api/email/**")
                        .filters(f -> f.stripPrefix(2))
                        .uri("http://localhost:8085"))

                .build();
    }

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("*");
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }

    @Bean
    public org.springframework.cloud.gateway.config.HttpClientCustomizer httpClientCustomizer() {
        return httpClient -> httpClient
                .responseTimeout(Duration.ofSeconds(30))
                .option(io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS, 10_000);
    }

}
