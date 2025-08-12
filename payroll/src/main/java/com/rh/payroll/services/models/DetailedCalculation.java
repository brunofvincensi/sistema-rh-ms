package com.rh.payroll.services.models;

import com.rh.payroll.clients.employee.EmployeeResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class DetailedCalculation {

    private EmployeeResponse employeeResponse;
    private BigDecimal consolidatedSalary;
    private BigDecimal baseSalary;

}