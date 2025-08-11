package com.rh.payroll.producers;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class PayrollRecordEvent {

    private UUID id;
    private UUID employeeId;
    private String cpf;
    private String role;

}
