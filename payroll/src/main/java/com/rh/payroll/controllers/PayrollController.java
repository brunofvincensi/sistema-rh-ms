package com.rh.payroll.controllers;

import com.rh.payroll.dtos.PayrollRecordInfoResponse;
import com.rh.payroll.dtos.PayrollRecordRequest;
import com.rh.payroll.dtos.PayrollRecordResponse;
import com.rh.payroll.services.PayrollService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController("/users")
public class PayrollController {

    private final PayrollService payrollService;

    public PayrollController(PayrollService payrollService) {
        this.payrollService = payrollService;
    }

    @PostMapping
    public ResponseEntity<?> record(@RequestBody @Valid PayrollRecordRequest payrollRecordRequest) {
        UUID recordId = payrollService.record(payrollRecordRequest);
        return ResponseEntity.accepted().body(new PayrollRecordResponse(recordId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") UUID payrollRecordId) {
        PayrollRecordInfoResponse infoResponse = payrollService.findById(payrollRecordId);
        return ResponseEntity.ok(infoResponse);
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<?> findAllBy(@PathVariable("id") UUID payrollRecordId) {
//        PayrollRecordInfoResponse infoResponse = payrollService.findById(payrollRecordId);
//        return ResponseEntity.ok(infoResponse);
//    }

}
