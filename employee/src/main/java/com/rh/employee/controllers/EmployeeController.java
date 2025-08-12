package com.rh.employee.controllers;

import com.rh.common.HeaderConstants;
import com.rh.employee.dtos.EmployeeRequest;
import com.rh.employee.dtos.EmployeeResponse;
import com.rh.employee.dtos.EmployeeUpdateRequest;
import com.rh.employee.services.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final HttpServletRequest httpServletRequest;
    private final EmployeeService employeeService;

    public EmployeeController(HttpServletRequest httpServletRequest, EmployeeService employeeService) {
        this.httpServletRequest = httpServletRequest;
        this.employeeService = employeeService;
    }

    @PostMapping("/admit")
    public ResponseEntity<?> admit(@RequestBody @Valid EmployeeRequest employeeRequest) {
        employeeService.admit(employeeRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") String id, @RequestBody @Valid EmployeeUpdateRequest employeeRequest) {
        employeeService.update(UUID.fromString(id), employeeRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/me")
    public ResponseEntity<?> findByMe() {
        String employeeIdHeader = httpServletRequest.getHeader(HeaderConstants.EMPLOYEE_ID);
        EmployeeResponse infoResponse = employeeService.findById(UUID.fromString(employeeIdHeader));
        return ResponseEntity.ok(infoResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") String employeeId) {
        EmployeeResponse infoResponse = employeeService.findById(UUID.fromString(employeeId));
        return ResponseEntity.ok(infoResponse);
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(employeeService.findAll());
    }

}
