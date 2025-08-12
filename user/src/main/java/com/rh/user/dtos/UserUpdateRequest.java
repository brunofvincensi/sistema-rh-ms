package com.rh.user.dtos;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

public record UserUpdateRequest(@NotBlank @CPF String cpf,
                                String role) {
}
