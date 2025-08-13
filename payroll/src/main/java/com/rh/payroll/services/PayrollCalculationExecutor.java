package com.rh.payroll.services;

import com.rh.payroll.clients.employee.EmployeeResponse;
import com.rh.payroll.clients.time_clock.TimeEntryResponse;
import com.rh.payroll.services.models.DetailedCalculation;

import java.util.List;

public interface PayrollCalculationExecutor {

    DetailedCalculation execute(EmployeeResponse employee, List<TimeEntryResponse> timeEntries);

}
