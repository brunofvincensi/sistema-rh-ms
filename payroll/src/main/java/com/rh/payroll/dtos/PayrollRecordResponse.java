package com.rh.payroll.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class PayrollRecordResponse {

    private UUID id;

}
