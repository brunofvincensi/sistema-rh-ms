package com.rh.payroll.producers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PayrollEmailProducer {

    final RabbitTemplate rabbitTemplate;

    public PayrollEmailProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value(value = "${broker.queue.email.name}")
    private String routingKey;

    public void publishPayrollEmail(PayrollEmailEvent payrollEmailEvent) {
        rabbitTemplate.convertAndSend(routingKey, payrollEmailEvent);
    }

}
