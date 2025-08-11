package com.rh.user.producers;

import com.rh.user.models.UserEntity;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserUpdateProducer {

    private final RabbitTemplate rabbitTemplate;

    public UserUpdateProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value(value = "${broker.queue.user-update.name}")
    private String routingKey;

    public void publishUserUpdate(UserEntity user) {
        var userUpdatedEvent = new UserUpdatedEvent(user.getId(),user.getEmployeeId(), user.getCpf(), user.getRole());
        rabbitTemplate.convertAndSend(routingKey, userUpdatedEvent);
    }

}
