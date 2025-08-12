package com.rh.employee.services;

import com.rh.common.exceptions.BusinessException;
import com.rh.common.exceptions.ResourceNotFoundException;
import com.rh.employee.clients.user.UserApiClient;
import com.rh.employee.clients.user.UserRequest;
import com.rh.employee.clients.user.UserUpdateRequest;
import com.rh.employee.dtos.EmployeeRequest;
import com.rh.employee.dtos.EmployeeResponse;
import com.rh.employee.dtos.EmployeeUpdateRequest;
import com.rh.employee.models.EmployeeEntity;
import com.rh.employee.repositories.EmployeeRepository;
import com.rh.employee.repositories.WorkScheduleRepository;
import feign.FeignException;
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
    public void admit(EmployeeRequest request) {
        EmployeeEntity employee = newEmployee(request);
        employee = internalSave(employee);

        createUser(request, employee);
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

    @Override
    public void update(UUID id, EmployeeUpdateRequest request) {
        EmployeeEntity employee = employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Colaborador não encontrado"));

        employee.setName(request.name());
        employee.setCpf(request.cpf());
        employee.setEmail(request.email());
        employee.setBirthDate(request.birthDate());
        employee.setSalary(request.salary());
        employee.setWorkSchedule(workScheduleRepository.findById(request.workScheduleId()).orElse(null));

        employee = internalSave(employee);
    }

    @Override
    public EmployeeResponse findById(UUID employeeId) {
        EmployeeEntity employee = employeeRepository.findById(employeeId).orElseThrow(() -> new ResourceNotFoundException("Colaborador não encontrado"));
        return new EmployeeResponse(employee);
    }

    @Override
    public List<EmployeeResponse> findAll() {
        return employeeRepository.findAll().stream().map(EmployeeResponse::new).toList();
    }

    private void createUser(EmployeeRequest request, EmployeeEntity employee) {
        UserRequest userRequest = new UserRequest(employee.getId(), employee.getCpf(), request.password(), request.role());
        try {
            userApiClient.createUser(userRequest);
        } catch (FeignException e) {
            String message = e.contentUTF8(); // corpo da resposta com a mensagem de erro
            throw new BusinessException("Falha ao criar usuário: " + message);
        }
    }

    private void updateUser(EmployeeUpdateRequest request, EmployeeEntity employee) {
        UserUpdateRequest userRequest = new UserUpdateRequest(employee.getCpf(), request.role());
        try {
            userApiClient.updateByIdEmployee(employee.getId().toString(), userRequest);
        } catch (FeignException e) {
            String message = e.contentUTF8(); // corpo da resposta com a mensagem de erro
            throw new BusinessException("Falha ao alterar usuário: " + message);
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
