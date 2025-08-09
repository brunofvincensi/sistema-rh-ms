package com.rh.employee.services;

import com.rh.employee.dtos.EmployeeRequest;
import com.rh.employee.models.EmployeeEntity;
import com.rh.employee.repositories.EmployeeRepository;
import com.rh.employee.repositories.WorkScheduleRepository;
import com.rh.employee.user.client.UserApiClient;
import com.rh.employee.user.dto.UserRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
        try {
            // 1. Valida dados únicos
            validateUniqueFields(request);

            // 2. Cria colaborador
            EmployeeEntity employee = EmployeeEntity.builder()
                    .name(request.name())
                    .cpf(request.cpf())
                    .email(request.email())
                    .birthDate(request.birthDate())
                    .admissionDate(request.admissionDate())
                    .salary(request.salary())
                    .workSchedule(workScheduleRepository.findById(request.workScheduleId()).orElse(null))
                    .build();

            EmployeeEntity savedEmployee = employeeRepository.save(employee);

            // 3. Cria usuário via API interna
            UserRequest userRequest = new UserRequest(savedEmployee.getId(), savedEmployee.getCpf(), request.password(), request.role());

            ResponseEntity<Void> userResponse = userApiClient.createUser(userRequest);

            return savedEmployee;

        } catch (Exception e) {
            throw e;
            // Rollback automático por @Transactional
           // throw new EmployeeRegistrationException("Falha ao registrar colaborador", e);
        }
    }

    private void validateUniqueFields(EmployeeRequest request)  {
        if (employeeRepository.existsByCpf(request.cpf())) {
            throw new RuntimeException("CPF já cadastrado");
        }
        if (employeeRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email já cadastrado");
        }
    }

    @Override
    public List<EmployeeEntity> findAll() {
        return List.of();
    }

}
