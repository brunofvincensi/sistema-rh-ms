package com.rh.api_gateway.services;

import com.rh.api_gateway.dtos.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private ReactiveRedisTemplate<String, Object> redisTemplate;

    @Autowired
    private WebClient.Builder webClientBuilder;

    private static final String CACHE_PREFIX = "user_permission:";
    // Timeout do cache
    private static final Duration CACHE_TTL = Duration.ofMinutes(30);

    @Override
    public Mono<UserInfo> getUserPermission(String userId) {
        String cacheKey = CACHE_PREFIX + userId;

        // Primeiro, tentar buscar do cache Redis
        return redisTemplate.opsForValue()
                .get(cacheKey)
                .cast(UserInfo.class)
                .switchIfEmpty(
                        // Se não encontrar no cache, buscar do serviço
                        findUserPermission(userId)
                                .flatMap(userInfo ->
                                        // Salvar no cache e retornar
                                        redisTemplate.opsForValue()
                                                .set(cacheKey, userInfo, CACHE_TTL)
                                                .thenReturn(userInfo)
                                )
                );
    }

    private Mono<UserInfo> findUserPermission(String userId) {
        return webClientBuilder.build()
                .get()
                .uri("http://localhost:8081/users/" + userId)
                .retrieve()
                .bodyToMono(UserInfo.class)
                .onErrorReturn(new UserInfo());
    }

    @Override
    public Mono<Void> invalidateCache(String userId) {
        String cacheKey = CACHE_PREFIX + userId;
        return redisTemplate.delete(cacheKey).then();
    }

}
