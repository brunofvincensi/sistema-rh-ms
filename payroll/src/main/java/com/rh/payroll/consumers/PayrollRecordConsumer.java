package com.rh.payroll.consumers;

import com.rh.payroll.clients.employee.EmployeeApiCliente;
import com.rh.payroll.clients.employee.EmployeeResponse;
import com.rh.payroll.clients.time_clock.TimeClockApiCliente;
import com.rh.payroll.clients.time_clock.TimeEntryResponse;
import com.rh.payroll.domain.enums.PayrollRecordStatus;
import com.rh.payroll.domain.models.PayrollRecordEntity;
import com.rh.payroll.repositories.PayrollRecordRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class PayrollRecordConsumer {

    private final PayrollRecordRepository payrollRecordRepository;
    private final TimeClockApiCliente timeClockApiCliente;
    private final EmployeeApiCliente employeeApiCliente;

    public PayrollRecordConsumer(PayrollRecordRepository payrollRecordRepository, TimeClockApiCliente timeClockApiCliente, EmployeeApiCliente employeeApiCliente) {
        this.payrollRecordRepository = payrollRecordRepository;
        this.timeClockApiCliente = timeClockApiCliente;
        this.employeeApiCliente = employeeApiCliente;
    }

    @RabbitListener(queues = "${broker.queue.payroll-record.name}")
    public void listenRecord(@Payload UUID recordId) {
        PayrollRecordEntity payrollRecord = payrollRecordRepository.findById(recordId).orElse(null);
        if (payrollRecord == null) {
            return;
        }

        UUID employeeId = payrollRecord.getEmployeeId();

        ResponseEntity<?> timeEntriesResponse = timeClockApiCliente.findTimeEntriesByMonthAndYear(payrollRecord.getEmployeeId().toString(), payrollRecord.getMonth(), payrollRecord.getYear());
        if (!timeEntriesResponse.getStatusCode().is2xxSuccessful()) {
            payrollRecord.setErrorMessage("Erro ao buscar os registros de ponto");
            payrollRecord.setStatus(PayrollRecordStatus.FAILED);
        }
        List<TimeEntryResponse> timeEntries = (List<TimeEntryResponse>) timeEntriesResponse.getBody();

        ResponseEntity<?> employeeResponse = employeeApiCliente.findById(payrollRecord.getEmployeeId());
        if (!employeeResponse.getStatusCode().is2xxSuccessful()) {
            payrollRecord.setErrorMessage("Erro ao buscar os dados do colaborador");
            payrollRecord.setStatus(PayrollRecordStatus.FAILED);
        }
        EmployeeResponse employee = (EmployeeResponse) timeEntriesResponse.getBody();




    }

}
