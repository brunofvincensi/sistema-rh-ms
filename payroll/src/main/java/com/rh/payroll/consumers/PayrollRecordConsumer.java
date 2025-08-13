package com.rh.payroll.consumers;

import com.rh.common.exceptions.ResourceNotFoundException;
import com.rh.payroll.clients.employee.EmployeeApiClient;
import com.rh.payroll.clients.employee.EmployeeResponse;
import com.rh.payroll.clients.time_clock.TimeClockApiClient;
import com.rh.payroll.clients.time_clock.TimeEntryResponse;
import com.rh.payroll.domain.enums.PayrollRecordStatus;
import com.rh.payroll.domain.models.PayrollRecordEntity;
import com.rh.payroll.producers.PayrollEmailEvent;
import com.rh.payroll.producers.PayrollEmailProducer;
import com.rh.payroll.producers.PayrollRecordEvent;
import com.rh.payroll.repositories.PayrollRecordRepository;
import com.rh.payroll.services.PayrollCalculationExecutor;
import com.rh.payroll.services.PayrollCalculationExecutorFactory;
import com.rh.payroll.services.models.DetailedCalculation;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Component
public class PayrollRecordConsumer {

    private final PayrollRecordRepository payrollRecordRepository;
    private final TimeClockApiClient timeClockApiClient;
    private final EmployeeApiClient employeeApiClient;
    private final PayrollEmailProducer emailProducer;

    public PayrollRecordConsumer(PayrollRecordRepository payrollRecordRepository, TimeClockApiClient timeClockApiClient, EmployeeApiClient employeeApiClient, PayrollEmailProducer emailProducer) {
        this.payrollRecordRepository = payrollRecordRepository;
        this.timeClockApiClient = timeClockApiClient;
        this.employeeApiClient = employeeApiClient;
        this.emailProducer = emailProducer;
    }

    @RabbitListener(queues = "${broker.queue.payroll-record.name}")
    public void listenRecord(@Payload PayrollRecordEvent event) {
        UUID recordId = event.getId();
        PayrollRecordEntity payrollRecord = payrollRecordRepository.findById(recordId).orElse(null);
        if (payrollRecord == null) {
            return;
        }

        try {
            ResponseEntity<EmployeeResponse> employeeResponse = employeeApiClient.findById(payrollRecord.getEmployeeId().toString());
            EmployeeResponse employee = employeeResponse.getBody();
            if (employee == null) {
                throw new ResourceNotFoundException("Colaborador não encontrado");
            }

            ResponseEntity<List<TimeEntryResponse>> timeEntriesResponse = timeClockApiClient.findByMonthAndYear(payrollRecord.getEmployeeId().toString(),
                    payrollRecord.getMonth(), payrollRecord.getYear());
            List<TimeEntryResponse> timeEntries = timeEntriesResponse.getBody();
            if (timeEntries == null || timeEntries.isEmpty()) {
                throw new ResourceNotFoundException("Pontos não cadastrados");
            }

            PayrollCalculationExecutor executor = PayrollCalculationExecutorFactory.getInstance();
            DetailedCalculation detailedCalculation = executor.execute(employee, timeEntries);

            payrollRecord.setBaseSalary(detailedCalculation.getBaseSalary());
            payrollRecord.setConsolidatedSalary(detailedCalculation.getConsolidatedSalary());
            payrollRecord.setStatus(PayrollRecordStatus.COMPLETED);

            // Envia os e-mails
            sendEmail(employee, event.getActionEmployeeId());
        } catch (Exception e) {
            payrollRecord.setErrorMessage(e.getMessage());
            payrollRecord.setStatus(PayrollRecordStatus.FAILED);
        } finally {
            payrollRecord.setFinishedAt(LocalDate.now());
            payrollRecordRepository.save(payrollRecord);
        }
    }

    private void sendEmail(EmployeeResponse employee, UUID actionIdEmployee) {
        PayrollEmailEvent emailEventToPayrollEmployee = new PayrollEmailEvent(employee.getEmail(), "Folha de pagamento", "A sua folha de pagamento foi gerada!");
        emailProducer.publishPayrollEmail(emailEventToPayrollEmployee);

        ResponseEntity<EmployeeResponse> actionEmployeeResponse = employeeApiClient.findById(actionIdEmployee.toString());
        EmployeeResponse actionEmployee = actionEmployeeResponse.getBody();
        if (actionEmployee != null) {
            PayrollEmailEvent emailEventToActionEmployee = new PayrollEmailEvent(actionEmployee.getEmail(), "Folha de pagamento", String.format("A folha de pagamento do %s foi gerada!", employee.getName()));
            emailProducer.publishPayrollEmail(emailEventToActionEmployee);
        }
    }

}
