package com.rh.employee.services;

import com.rh.employee.dtos.EmployeeRequest;
import com.rh.employee.dtos.EmployeeResponse;

import java.util.List;
import java.util.UUID;

public interface EmployeeService {

    void adimit(EmployeeRequest employeeRequest);

    EmployeeResponse findById(UUID employeeId);

    List<EmployeeResponse> findAll();
}
