package com.rh.api_gateway.services;

import com.rh.api_gateway.dtos.UserInfo;
import reactor.core.publisher.Mono;

public interface PermissionService {

    Mono<UserInfo> getUserPermission(String usuarioId);

    Mono<Void> invalidateCache(String usuarioId);

}
