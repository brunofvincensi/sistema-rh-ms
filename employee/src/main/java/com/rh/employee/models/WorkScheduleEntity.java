package com.rh.employee.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
@Table(name = "TB_WORK_SCHEDULE")
public class WorkScheduleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotBlank
    @Column(name = "ds_name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "vl_hours", precision = 4, scale = 2, nullable = false)
    private BigDecimal hours;

    @NotNull
    @Column(name = "nr_days", nullable = false)
    private Integer days;

}
