package com.rh.payroll.clients.employee;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "employee-service")
public interface EmployeeApiClient {

    @GetMapping("/employees/{id}")
    ResponseEntity<EmployeeResponse> findById(@PathVariable("id") String employeeId);

}
