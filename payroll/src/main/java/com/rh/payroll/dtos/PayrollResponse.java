package com.rh.payroll.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PayrollResponse {

    private BigDecimal consolidatedSalary;
    private BigDecimal baseSalary;

}
