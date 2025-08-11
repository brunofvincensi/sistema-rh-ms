package com.rh.employee.services;

import com.rh.common.exceptions.BusinessException;
import com.rh.common.exceptions.ResourceNotFoundException;
import com.rh.employee.dtos.EmployeeRequest;
import com.rh.employee.dtos.EmployeeResponse;
import com.rh.employee.models.EmployeeEntity;
import com.rh.employee.repositories.EmployeeRepository;
import com.rh.employee.repositories.WorkScheduleRepository;
import com.rh.employee.clients.user.UserApiClient;
import com.rh.employee.clients.user.UserRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

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
    @Transactional(rollbackFor = Throwable.class)
    public void adimit(EmployeeRequest request) {
        EmployeeEntity employee = newEmployee(request);
        employee = internalSave(employee);

        createUser(request, employee);
    }

    @Override
    public EmployeeResponse findById(UUID employeeId) {
        EmployeeEntity employee = employeeRepository.findById(employeeId).orElseThrow(() -> new ResourceNotFoundException("Colaborador não encontrado"));
        return new EmployeeResponse(); // TODO
    }

    @Override
    public List<EmployeeResponse> findAll() {
        return employeeRepository.findAll();
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
        if (!userResponse.getStatusCode().is2xxSuccessful()) {
            // Lança uma exceção para dar rollback na transação
            throw new BusinessException(
                    "Falha ao criar usuário: " + userResponse.getStatusCode()
            );
        }
    }

    private EmployeeEntity internalSave(EmployeeEntity employee) {
        validaUniqueFields(employee);
        return employeeRepository.save(employee);
    }

    private void validaUniqueFields(EmployeeEntity employee) {
        if (employeeRepository.existsByCpf(employee.getCpf())) {
            throw new BusinessException("CPF já cadastrado");
        }
        if (employeeRepository.existsByEmail(employee.getEmail())) {
            throw new BusinessException("Email já cadastrado");
        }
    }

}
