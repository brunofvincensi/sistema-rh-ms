package com.rh.payroll.services;

import com.rh.payroll.dtos.PayrollRecordInfoResponse;
import com.rh.payroll.dtos.PayrollRecordRequest;

import java.util.UUID;

public interface PayrollService {

    UUID record(PayrollRecordRequest payrollRecordRequest);

    PayrollRecordInfoResponse findById(UUID userId);

}
