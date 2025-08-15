package com.rh.api_gateway.config.security;

import static com.rh.common.users.RoleConstants.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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

                        // Eureka
                        .pathMatchers("/eureka/**").permitAll()

                        // Employee
                        .pathMatchers("/api/employee/employees/admit").permitAll()
                        .pathMatchers( "/api/employee/work-schedules/**").permitAll()

                        .pathMatchers(HttpMethod.GET, "/api/employee/employees/me").hasAnyRole(COLABORADOR, ANALISTA_RH)

                        .pathMatchers(HttpMethod.GET, "/api/employee/employees").hasAnyRole(ANALISTA_RH)
                        .pathMatchers(HttpMethod.GET, "/api/employee/employees/{id}").hasAnyRole(ANALISTA_RH)
                        .pathMatchers(HttpMethod.PUT, "/api/employee/employees/{id}").hasAnyRole(ANALISTA_RH)

                        // Time Clock
                        .pathMatchers(HttpMethod.POST, "/api/time-entry/time-entries/record").hasAnyRole(COLABORADOR, ANALISTA_RH)
                        .pathMatchers(HttpMethod.PUT, "/api/time-entry/time-entries/record/{id}").hasAnyRole(COLABORADOR, ANALISTA_RH)
                        .pathMatchers(HttpMethod.GET, "/api/time-entry/time-entries/daily/me").hasAnyRole(COLABORADOR, ANALISTA_RH)

                        .pathMatchers(HttpMethod.GET, "/api/time-entry/time-entries/daily").hasAnyRole(ANALISTA_RH)
                        .pathMatchers(HttpMethod.GET, "/api/time-entry/time-entries/monthly").hasAnyRole(ANALISTA_RH)

                        // Payroll
                        .pathMatchers(HttpMethod.GET, "/api/payroll/payrolls/me/**").hasAnyRole(COLABORADOR, ANALISTA_RH)

                        .pathMatchers("/api/payroll/payrolls/record/**").hasAnyRole(ANALISTA_RH)
                        .pathMatchers(HttpMethod.GET, "/api/payroll/payrolls/{id}").hasAnyRole(ANALISTA_RH)
                        .pathMatchers(HttpMethod.GET, "/api/payroll/payrolls").hasAnyRole(ANALISTA_RH)

                        // User não é permitido acessar diretamente
                        // Demais rotas protegidas
                        .anyExchange().authenticated()
                )
                .addFilterBefore(authenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

}
