package com.rh.payroll.producers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayrollRecordEvent {

    private UUID id;
    private UUID actionEmployeeId;

}
