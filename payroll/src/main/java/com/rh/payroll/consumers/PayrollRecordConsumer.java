package com.rh.payroll.consumers;

import com.rh.payroll.domain.enums.PayrollRecordStatus;
import com.rh.payroll.domain.models.PayrollRecordEntity;
import com.rh.payroll.repositories.PayrollRecordRepository;
import com.rh.payroll.services.PayrollExecutorFactory;
import com.rh.payroll.services.models.DetailedCalculation;
import com.rh.payroll.services.PayrollExecutor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

@Component
public class PayrollRecordConsumer {

    private final PayrollRecordRepository payrollRecordRepository;
    private final PayrollExecutorFactory payrollExecutorFactory;

    public PayrollRecordConsumer(PayrollRecordRepository payrollRecordRepository, PayrollExecutorFactory payrollExecutorFactory) {
        this.payrollRecordRepository = payrollRecordRepository;
        this.payrollExecutorFactory = payrollExecutorFactory;
    }

    @RabbitListener(queues = "${broker.queue.payroll-record.name}")
    public void listenRecord(@Payload UUID recordId) {
        PayrollRecordEntity payrollRecord = payrollRecordRepository.findById(recordId).orElse(null);
        if (payrollRecord == null) {
            return;
        }

        try {
            PayrollExecutor executor = payrollExecutorFactory.getInstance(payrollRecord.getMonth(), payrollRecord.getYear());

            DetailedCalculation calculoDetalhado = executor.execute(payrollRecord.getEmployeeId());

            payrollRecord.setBaseSalary(calculoDetalhado.getBaseSalary());
            payrollRecord.setConsolidatedSalary(calculoDetalhado.getConsolidatedSalary());
            payrollRecord.setStatus(PayrollRecordStatus.COMPLETED);
        } catch (Exception e) {
            payrollRecord.setErrorMessage(e.getMessage());
            payrollRecord.setStatus(PayrollRecordStatus.FAILED);
        } finally {
            payrollRecord.setFinishedAt(LocalDate.now());
        }
    }

}
