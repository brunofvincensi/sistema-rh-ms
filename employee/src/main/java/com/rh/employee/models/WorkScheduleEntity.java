package com.rh.employee.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "TB_WORK_SCHEDULE")
public class WorkScheduleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    @Column(name = "hours", precision = 4, scale = 2, nullable = false)
    private BigDecimal hours;

    @NotNull
    @Column(name = "days", nullable = false)
    private Integer days;

}
