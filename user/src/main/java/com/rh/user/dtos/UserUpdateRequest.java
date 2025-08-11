package com.rh.user.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;

import java.util.UUID;

public record UserUpdateRequest(@NotNull UUID id,
                                @NotBlank @CPF String cpf,
                                String role) {
}
