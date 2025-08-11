package com.rh.payroll.clients.employee;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class WorkScheduleResponse {

    private UUID id;
    private BigDecimal hours;
    private Integer days;

}
