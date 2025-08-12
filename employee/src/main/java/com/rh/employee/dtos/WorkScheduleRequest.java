package com.rh.employee.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record WorkScheduleRequest(@NotBlank String name,
                                   @NotNull BigDecimal hours,
                                   @NotNull Integer days) {
}
