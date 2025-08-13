package com.rh.payroll.clients.time_clock;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
public class TimeEntryResponse {

    private UUID id;
    private UUID employeeId;
    private LocalDate date;
    private LocalTime time;
    private String type;
    private String observation;
}
