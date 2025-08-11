package com.rh.api_gateway.config;

import com.rh.api_gateway.services.PermissionService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Component
public class AuthenticationFilter implements WebFilter {

    private final PermissionService permissaoService;

    public AuthenticationFilter(PermissionService permissaoService) {
        this.permissaoService = permissaoService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        // Permitir rotas públicas
        if (path.contains("/auth/") || path.contains("/eureka")) {
            return chain.filter(exchange);
        }

        // Simular header de usuário conforme especificação
        String usuarioHeader = exchange.getRequest().getHeaders().getFirst("X-Usuario-Id");

        if (usuarioHeader == null || usuarioHeader.trim().isEmpty()) {
            exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // Buscar dados do usuário no cache/serviço
        return permissaoService.getUserPermission(usuarioHeader)
                .flatMap(userInfo -> {
                    if (userInfo == null) {
                        exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
                        return exchange.getResponse().setComplete();
                    }

                    // Adicionar dados do usuário no header para os microserviços
                    ServerWebExchange mutatedExchange = exchange.mutate()
                            .request(originalRequest -> originalRequest
                                    .header("X-Usuario-Id", userInfo.getId().toString())
                                    .header("X-Usuario-Role", userInfo.getRole())
                                    .header("X-Colaborador-Id", userInfo.getEmployeeId().toString())
                            )
                            .build();

                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + userInfo.getRole());
                    Authentication auth = new UsernamePasswordAuthenticationToken(
                            userInfo.getId(), null, Collections.singletonList(authority));

                    return chain.filter(mutatedExchange)
                            .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));
                })
                .onErrorResume(error -> {
                    exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
                    return exchange.getResponse().setComplete();
                });
    }

}
