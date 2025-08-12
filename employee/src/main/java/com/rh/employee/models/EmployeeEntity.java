package com.rh.employee.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Table(name = "TB_EMPLOYEE")
public class EmployeeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotBlank
    @Column(name = "ds_name", nullable = false)
    private String name;

    @CPF
    @Column(name = "ds_cpf", nullable = false, unique = true)
    private String cpf;

    @Email
    @Column(name = "ds_email", nullable = false, unique = true)
    private String email;

    @NotNull
    @Column(name = "dt_birth_date", nullable = false)
    private LocalDate birthDate;

    @NotNull
    @Column(name = "dt_admission_date", nullable = false)
    private LocalDate admissionDate;

    @NotNull
    @Column(name = "vl_salary", nullable = false)
    private BigDecimal salary;

    @NotNull
    @ManyToOne(targetEntity = WorkScheduleEntity.class, cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "work_schedule_id", nullable = false)
    private WorkScheduleEntity workSchedule;

}
