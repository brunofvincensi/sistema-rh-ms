package com.rh.employee.dtos;

import com.rh.employee.models.WorkScheduleEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class WorkScheduleResponse {

    private UUID id;
    private BigDecimal hours;
    private Integer days;

    public WorkScheduleResponse(WorkScheduleEntity workSchedule) {
        this.id = workSchedule.getId();
        this.hours = workSchedule.getHours();
        this.days = workSchedule.getDays();
    }

}
