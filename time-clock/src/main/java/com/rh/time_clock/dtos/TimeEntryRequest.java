package com.rh.time_clock.dtos;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record TimeEntryRequest(@NotNull UUID employeeId,
                               String observation) {

}
