package com.rh.employee.configs;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

public class FeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
//            // Adiciona o JWT do contexto de segurança (se necessário)
//            if (SecurityContextHolder.getContext().getAuthentication() != null
//                    && SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof Jwt) {
//                Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//                requestTemplate.header("Authorization", "Bearer " + jwt.getTokenValue());
//            }
        };
    }

}
