package com.rh.employee.services;

import com.rh.employee.dtos.EmployeeRequest;
import com.rh.employee.models.EmployeeEntity;
import com.rh.employee.repositories.EmployeeRepository;
import com.rh.employee.repositories.WorkScheduleRepository;
import com.rh.employee.user.client.UserApiClient;
import com.rh.employee.user.dto.UserRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final WorkScheduleRepository workScheduleRepository;
    private final UserApiClient userApiClient;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, WorkScheduleRepository workScheduleRepository, UserApiClient userApiClient) {
        this.employeeRepository = employeeRepository;
        this.workScheduleRepository = workScheduleRepository;
        this.userApiClient = userApiClient;
    }

    @Override
    public EmployeeEntity adimit(EmployeeRequest request) {
        EmployeeEntity employee = newEmployee(request);
        employee = internalSave(employee);

        createUser(request, employee);

        return employee;
    }

    private EmployeeEntity newEmployee(EmployeeRequest request) {
        EmployeeEntity employee = new EmployeeEntity();
        employee.setName(request.name());
        employee.setCpf(request.cpf());
        employee.setEmail(request.email());
        employee.setBirthDate(request.birthDate());
        employee.setAdmissionDate(LocalDate.now());
        employee.setSalary(request.salary());
        employee.setWorkSchedule(workScheduleRepository.findById(request.workScheduleId()).orElse(null));
        return employee;
    }

    private void createUser(EmployeeRequest request, EmployeeEntity employee) {
        UserRequest userRequest = new UserRequest(employee.getId(), employee.getCpf(), request.password(), request.role());
        ResponseEntity<Void> userResponse = userApiClient.createUser(userRequest);
    }

    private EmployeeEntity internalSave(EmployeeEntity employee) {
        validaUniqueFields(employee);
        return employeeRepository.save(employee);
    }

    private void validaUniqueFields(EmployeeEntity employee) {
        if (employeeRepository.existsByCpf(employee.getCpf())) {
            throw new RuntimeException("CPF já cadastrado");
        }
        if (employeeRepository.existsByEmail(employee.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }
    }

    @Override
    public List<EmployeeEntity> findAll() {
        return employeeRepository.findAll();
    }

}
