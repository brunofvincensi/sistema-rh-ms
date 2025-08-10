package com.rh.api_gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Autowired
    private AuthenticationFilter authenticationFilter;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .authorizeExchange(exchanges -> exchanges
                        // Rotas públicas
                        .pathMatchers("/api/permissao/auth/**").permitAll()
                        .pathMatchers("/eureka/**").permitAll()
                        .pathMatchers("/api/emplyee/**").permitAll()

//                        // Rotas do Colaborador
//                        .pathMatchers("/api/ponto/colaborador/**").hasAnyRole("COLABORADOR", "ANALISTA_RH")
//                        .pathMatchers("/api/admissao/colaborador/me").hasAnyRole("COLABORADOR", "ANALISTA_RH")
//                        .pathMatchers("/api/folha/colaborador/me").hasAnyRole("COLABORADOR", "ANALISTA_RH")
//
//                        // Rotas exclusivas do Analista RH
//                        .pathMatchers("/api/admissao/**").hasRole("ANALISTA_RH")
//                        .pathMatchers("/api/folha/calcular/**").hasRole("ANALISTA_RH")
//                        .pathMatchers("/api/folha/todos/**").hasRole("ANALISTA_RH")
//                        .pathMatchers("/api/ponto/todos/**").hasRole("ANALISTA_RH")

                        // Demais rotas protegidas
                        .anyExchange().authenticated()
                )
                .addFilterBefore(authenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

}
