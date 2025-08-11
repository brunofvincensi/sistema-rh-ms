package com.rh.user.dtos;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

import java.util.UUID;

public record UserRequest(UUID employeeId,
                          @NotBlank @CPF String cpf,
                          @NotBlank String password,
                          String role) {

}