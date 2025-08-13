package com.rh.employee.services;

import com.rh.employee.dtos.EmployeeRequest;
import com.rh.employee.dtos.EmployeeResponse;
import com.rh.employee.dtos.EmployeeUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface EmployeeService {

    EmployeeResponse admit(EmployeeRequest employeeRequest);

    EmployeeResponse update(UUID id, EmployeeUpdateRequest employeeRequest);

    EmployeeResponse findById(UUID employeeId);

    List<EmployeeResponse> findAll();
}
