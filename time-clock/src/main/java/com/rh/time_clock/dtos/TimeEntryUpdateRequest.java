package com.rh.time_clock.dtos;


import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record TimeEntryUpdateRequest(@NotNull LocalDate date,
                                      @NotNull LocalTime time,
                                      String observation) {

}