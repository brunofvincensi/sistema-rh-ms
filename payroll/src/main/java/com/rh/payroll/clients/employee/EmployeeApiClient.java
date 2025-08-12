package com.rh.payroll.clients.employee;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "employee",
        url = "${employee.api.url}"
)
public interface EmployeeApiClient {

    @GetMapping("/employees/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") String employeeId);

}
