package com.rh.payroll.producers;

import com.rh.payroll.domain.models.PayrollRecordEntity;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PayrollRecordProducer {

    private final RabbitTemplate rabbitTemplate;

    public PayrollRecordProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value(value = "${broker.queue.payroll-record.name}")
    private String routingKey;

    public void publishRecord(PayrollRecordEntity payrollRecord, UUID actionEmployeeId) {
        PayrollRecordEvent event = new PayrollRecordEvent(payrollRecord.getId(), actionEmployeeId);
        rabbitTemplate.convertAndSend(routingKey, event);
    }

}
