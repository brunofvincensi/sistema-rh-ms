package com.rh.time_clock.domain.models;

import com.rh.time_clock.domain.enums.TimeEntryType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "TB_TIME_ENTRY",
        indexes = {
                @Index(name = "idx_employee_date", columnList = "employee_id, dt_date")
        })
public class TimeEntryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotBlank
    @Column(name = "employee_id", nullable = false)
    private UUID employeeId;

    @NotNull
    @Column(name = "dt_date", nullable = false)
    private LocalDate date;

    @NotNull
    @Column(name = "hr_time", nullable = false)
    private LocalTime time;

    @NotNull
    @Column(name = "tp_time_entry", nullable = false)
    private TimeEntryType type;

    @Column(name = "ds_observation", nullable = true)
    private String observation;

}
