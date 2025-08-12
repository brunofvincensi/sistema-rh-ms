package com.rh.payroll.services;

import com.rh.common.exceptions.BusinessException;
import com.rh.common.exceptions.ResourceNotFoundException;
import com.rh.payroll.clients.employee.EmployeeApiClient;
import com.rh.payroll.clients.employee.EmployeeResponse;
import com.rh.payroll.clients.employee.WorkScheduleResponse;
import com.rh.payroll.clients.time_clock.TimeClockApiClient;
import com.rh.payroll.clients.time_clock.TimeEntryResponse;
import com.rh.payroll.services.models.DetailedCalculation;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class PayrollExecutor {

    private final Integer month;
    private final Integer year;

    private final TimeClockApiClient timeClockApiClient;
    private final EmployeeApiClient employeeApiClient;

    public PayrollExecutor(Integer month, Integer year, TimeClockApiClient timeClockApiClient, EmployeeApiClient employeeApiClient) {
        this.month = month;
        this.year = year;
        this.timeClockApiClient = timeClockApiClient;
        this.employeeApiClient = employeeApiClient;
    }

    public DetailedCalculation execute(UUID employeeId) {
        ResponseEntity<?> employeeResponse = employeeApiClient.findById(employeeId.toString());
        if (!employeeResponse.getStatusCode().is2xxSuccessful()) {
            throw new BusinessException("Erro ao buscar os dados do colaborador");
        }
        EmployeeResponse employee = (EmployeeResponse) employeeResponse.getBody();
        if (employee == null) {
            throw new ResourceNotFoundException("Colaborador não encontrado");
        }

        ResponseEntity<?> timeEntriesResponse = timeClockApiClient.findTimeEntriesByMonthAndYear(employeeId.toString(), month, year);
        if (!timeEntriesResponse.getStatusCode().is2xxSuccessful()) {
            throw new BusinessException("Erro ao buscar os registros de ponto");
        }
        List<TimeEntryResponse> timeEntries = (List<TimeEntryResponse>) timeEntriesResponse.getBody();
        if (timeEntries == null || timeEntries.isEmpty()) {
            throw new ResourceNotFoundException("Pontos não cadastrados");
        }

        return execute(employee, timeEntries);
    }

    private DetailedCalculation execute(EmployeeResponse employee, List<TimeEntryResponse> timeEntries) {
        List<ConsolidatedWorkDay> workDays = consolidateTimeEntriesByDay(timeEntries);

        BigDecimal actualHours = workDays.stream()
                .map(ConsolidatedWorkDay::getWorkedHours)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Processa cálculos
        return calculateSalary(
                employee,
                workDays.size(),
                actualHours
        );
    }

    /**
     * Consolida as marcações por dia, calculando horas trabalhadas
     */
    private List<ConsolidatedWorkDay> consolidateTimeEntriesByDay(List<TimeEntryResponse> timeEntries) {
        // Agrupa por data
        Map<LocalDate, List<TimeEntryResponse>> timeEntriesByDay = timeEntries.stream()
                .collect(Collectors.groupingBy(TimeEntryResponse::getDate));

        // Consolida cada dia
        return timeEntriesByDay.entrySet().stream()
                .map(entry -> new ConsolidatedWorkDay(entry.getKey(), entry.getValue()))
                .filter(ConsolidatedWorkDay::existsWorkedHours) // Só considera dias que possuem horas consolidadas
                .sorted(Comparator.comparing(ConsolidatedWorkDay::getDate))
                .collect(Collectors.toList());
    }

    /**
     * Calcula o salário com base na escala e dias consolidados
     */
    private DetailedCalculation calculateSalary(EmployeeResponse employee, Integer workedDays, BigDecimal actualHours) {
        if (employee == null || workedDays == null || actualHours == null) {
            return null;
        }

        WorkScheduleResponse workSchedule = employee.getWorkSchedule();

        BigDecimal baseSalary = employee.getSalary();
        Integer expectedDays = workSchedule.getDays();
        BigDecimal expectedHoursPerDay = workSchedule.getHours();

        // Calcula o salário proporcional aos dias trabalhados
        BigDecimal proportionalSalary = baseSalary
                .multiply(new BigDecimal(workedDays))
                .divide(new BigDecimal(expectedDays), 4, RoundingMode.HALF_UP);

        // Calcula as horas esperadas nos dias trabalhados
        BigDecimal expectedHours = expectedHoursPerDay
                .multiply(new BigDecimal(workedDays));

        // Calcula O valor da hora baseado no salário proporcional
        BigDecimal hourlyRate = proportionalSalary
                .divide(expectedHours, 4, RoundingMode.HALF_UP);

        // 4. Ajuste por diferença de horas
        BigDecimal hoursDifference = actualHours.subtract(expectedHours);
        BigDecimal hoursAdjustment = hourlyRate.multiply(hoursDifference);

        // 5. Salário consolidade do mês
        BigDecimal finalSalary = proportionalSalary.add(hoursAdjustment);

        return new DetailedCalculation(
                employee,
                finalSalary,
                baseSalary
        );
    }

}
