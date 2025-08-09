package com.rh.employee.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record EmployeeRequest(@NotBlank String name,
                              @NotBlank @CPF String cpf,
                              @NotBlank @Email String email,
                              @NotNull LocalDate birthDate,
                              @NotNull BigDecimal salary,
                              @NotNull UUID workScheduleId,
                              @NotBlank String password,
                              String role) {

}