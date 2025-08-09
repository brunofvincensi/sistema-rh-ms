package com.rh.user.dtos;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record UserRequest(UUID id,
                          UUID employeeId,
                          @NotBlank String cpf,
                          @NotBlank String password,
                          String role) {

}