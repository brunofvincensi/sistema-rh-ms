package com.rh.payroll.controllers;

import com.rh.common.HeaderConstants;
import com.rh.payroll.dtos.PayrollRecordInfoResponse;
import com.rh.payroll.dtos.PayrollRecordRequest;
import com.rh.payroll.dtos.PayrollRecordResponse;
import com.rh.payroll.dtos.PayrollResponse;
import com.rh.payroll.services.PayrollRecordService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/payrolls")
public class PayrollController {

    private final PayrollRecordService payrollService;
    private final HttpServletRequest httpServletRequest;

    public PayrollController(PayrollRecordService payrollService, HttpServletRequest httpServletRequest) {
        this.payrollService = payrollService;
        this.httpServletRequest = httpServletRequest;
    }

    @PostMapping("/record")
    public ResponseEntity<?> record(@RequestBody @Valid PayrollRecordRequest payrollRecordRequest) {
        String userId = httpServletRequest.getHeader(HeaderConstants.USER_ID);
        String employeeId = httpServletRequest.getHeader(HeaderConstants.EMPLOYEE_ID);
        UUID recordId = payrollService.record(payrollRecordRequest, UUID.fromString(userId), UUID.fromString(employeeId));
        return ResponseEntity.accepted().body(new PayrollRecordResponse(recordId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") UUID payrollRecordId) {
        PayrollRecordInfoResponse infoResponse = payrollService.findById(payrollRecordId);
        return ResponseEntity.ok(infoResponse);
    }

    @GetMapping("/me")
    public ResponseEntity<?> findByMe(@RequestBody @NotNull Integer month,
                                      @RequestBody @NotNull Integer year) {
        String employeeIdHeader = httpServletRequest.getHeader(HeaderConstants.EMPLOYEE_ID);
        PayrollResponse infoResponse = payrollService.findPayrollResult(UUID.fromString(employeeIdHeader), month, year);
        return ResponseEntity.ok(infoResponse);
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(payrollService.findAll());
    }

}
