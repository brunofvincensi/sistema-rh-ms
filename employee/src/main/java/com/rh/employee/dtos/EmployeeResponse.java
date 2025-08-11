package com.rh.employee.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class EmployeeResponse {

    private UUID id;
    private String name;
    private String cpf;
    private String email;
    private LocalDate birthDate;
    private LocalDate admissionDate;
    private BigDecimal salary;
    private WorkScheduleResponse workSchedule;

}
