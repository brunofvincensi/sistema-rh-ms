package com.rh.payroll.dtos;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record PayrollRecordRequest(@NotNull UUID employeeId,
                                   @NotNull Integer month,
                                   @NotNull Integer year) {


}