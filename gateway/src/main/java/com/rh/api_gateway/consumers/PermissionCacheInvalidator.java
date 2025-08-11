package com.rh.api_gateway.consumers;

import com.rh.api_gateway.services.PermissionService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PermissionCacheInvalidator {

    private final PermissionService permissaoService;

    public PermissionCacheInvalidator(PermissionService permissaoService) {
        this.permissaoService = permissaoService;
    }

    @RabbitListener(queues = "${broker.queue.user-update.name}")
    public void invalidateCache(String userId) {
        permissaoService.invalidateCache(userId);
    }

}
