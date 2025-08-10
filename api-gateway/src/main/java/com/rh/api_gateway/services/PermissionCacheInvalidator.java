package com.rh.api_gateway.services;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PermissionCacheInvalidator {

    private final PermissionService permissaoService;

    public PermissionCacheInvalidator(PermissionService permissaoService) {
        this.permissaoService = permissaoService;
    }

    @RabbitListener(queues = "user.permission.changed")
    public void invalidateCache(String userId) {
        permissaoService.invalidateCache(userId);
        // TODO gerar log
    }

}
