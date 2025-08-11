package com.rh.time_clock.dtos;

import com.rh.time_clock.domain.enums.TimeEntryType;
import com.rh.time_clock.domain.models.TimeEntryEntity;
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
    private TimeEntryType type;
    private String observation;

    public TimeEntryResponse(TimeEntryEntity entryEntity) {
        this.id = entryEntity.getId();
        this.employeeId = entryEntity.getEmployeeId();
        this.date = entryEntity.getDate();
        this.time = entryEntity.getTime();
        this.type = entryEntity.getType();
        this.observation = entryEntity.getObservation();
    }

}
