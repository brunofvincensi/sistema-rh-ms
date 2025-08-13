package com.rh.payroll.domain.models;

import com.rh.payroll.domain.enums.PayrollRecordStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Table(name = "TB_PAYROLL_RECORD")
public class PayrollRecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    @Column(name = "employee_id", nullable = false)
    private UUID employeeId;

    /*
     * Usuário que iniciou o cálculo
     */
    @NotNull
    @Column(name = "action_user_id", nullable = false)
    private UUID actionUserId;

    @NotNull
    @Column(name = "nr_month", nullable = false)
    private Integer month;

    @NotNull
    @Column(name = "nr_ano", nullable = false)
    private Integer year;

    @NotNull
    @Column(name = "dt_created_at", nullable = false)
    private LocalDate createdAt;

    @Column(name = "dt_finished_at", nullable = true)
    private LocalDate finishedAt;

    @NotNull
    @Column(name = "tp_status", nullable = false)
    private PayrollRecordStatus status;

    @Column(name = "ds_error_message", nullable = true)
    private String errorMessage;

    @Column(name = "vl_consolidated_salary", nullable = true)
    private BigDecimal consolidatedSalary;

    @Column(name = "vl_base_salary", nullable = true)
    private BigDecimal baseSalary;

}
