package com.rh.api_gateway.consumers;

import com.rh.api_gateway.dtos.UserInfo;
import com.rh.api_gateway.services.PermissionService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PermissionCacheInvalidator {

    private final PermissionService permissionService;

    public PermissionCacheInvalidator(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @RabbitListener(queues = "${broker.queue.user-update.name}")
    public void invalidateCache(UserInfo userInfo) {
        permissionService.invalidateCache(userInfo.getId().toString())
                .subscribe();
    }

}
