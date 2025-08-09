package com.rh.employee.services;

import com.rh.employee.dtos.EmployeeRequest;
import com.rh.employee.models.EmployeeEntity;

import java.util.List;

public interface EmployeeService {

    EmployeeEntity adimit(EmployeeRequest employeeRequest);

    List<EmployeeEntity> findAll();
}
