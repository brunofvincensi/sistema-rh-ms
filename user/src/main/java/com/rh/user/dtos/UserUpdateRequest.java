package com.rh.user.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UserUpdateRequest(@NotNull UUID id,
                                @NotBlank String cpf,
                                String role) {
}
