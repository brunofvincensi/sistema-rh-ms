package com.rh.payroll.dtos;

import com.rh.payroll.domain.enums.PayrollRecordStatus;
import com.rh.payroll.domain.models.PayrollRecordEntity;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class PayrollRecordInfoResponse {

    private UUID id;
    private UUID employeeId;
    private Integer month;
    private Integer year;
    private LocalDate createdAt;
    private LocalDate finishedAt;
    private PayrollRecordStatus status;
    private String errorMessage;

    public PayrollRecordInfoResponse(PayrollRecordEntity payrollRecordEntity) {
        this.id = payrollRecordEntity.getId();
        this.employeeId = payrollRecordEntity.getEmployeeId();
        this.month = payrollRecordEntity.getMonth();
        this.year = payrollRecordEntity.getYear();
        this.createdAt = payrollRecordEntity.getCreatedAt();
        this.finishedAt = payrollRecordEntity.getFinishedAt();
        this.status = payrollRecordEntity.getStatus();
        this.errorMessage = payrollRecordEntity.getErrorMessage();
    }

}
