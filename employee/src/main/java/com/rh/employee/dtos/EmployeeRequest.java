package com.rh.employee.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record EmployeeRequest(UUID id,
                              String name,
                              String cpf,
                              String email,
                              LocalDate birthDate,
                              LocalDate admissionDate,
                              BigDecimal salary,
                              UUID workScheduleId,
                              // Dados do usuário
                              String password, String role) {

}