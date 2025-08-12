package com.rh.employee.dtos;

import com.rh.employee.models.EmployeeEntity;
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

    public EmployeeResponse(EmployeeEntity employee) {
        this.id = employee.getId();
        this.name = employee.getName();
        this.cpf = employee.getCpf();
        this.email = employee.getEmail();
        this.birthDate = employee.getBirthDate();
        this.admissionDate = employee.getAdmissionDate();
        this.salary = employee.getSalary();
        this.workSchedule = new WorkScheduleResponse(employee.getWorkSchedule());
    }
}
