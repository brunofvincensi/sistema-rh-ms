package com.rh.payroll.services;

import com.rh.payroll.dtos.PayrollRecordInfoResponse;
import com.rh.payroll.dtos.PayrollRecordRequest;
import com.rh.payroll.dtos.PayrollResponse;

import java.util.List;
import java.util.UUID;

public interface PayrollRecordService {

    UUID record(PayrollRecordRequest payrollRecordRequest, UUID actionUserId, UUID actionEmployeeId);

    PayrollRecordInfoResponse findById(UUID id);

    List<PayrollRecordInfoResponse> findAll();

    PayrollResponse findPayrollResult(UUID employeeId, Integer month, Integer year);

}
