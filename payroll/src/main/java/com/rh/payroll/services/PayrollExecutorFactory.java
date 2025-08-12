package com.rh.payroll.services;

import com.rh.payroll.clients.employee.EmployeeApiClient;
import com.rh.payroll.clients.time_clock.TimeClockApiClient;
import org.springframework.stereotype.Component;

@Component
public class PayrollExecutorFactory {

    private final TimeClockApiClient timeClockApiClient;
    private final EmployeeApiClient employeeApiClient;

    public PayrollExecutorFactory(TimeClockApiClient timeClockApiClient, EmployeeApiClient employeeApiClient) {
        this.timeClockApiClient = timeClockApiClient;
        this.employeeApiClient = employeeApiClient;
    }

    public PayrollExecutor getInstance(Integer month, Integer year) {
        return new PayrollExecutor(month, year, timeClockApiClient, employeeApiClient);
    }

}
