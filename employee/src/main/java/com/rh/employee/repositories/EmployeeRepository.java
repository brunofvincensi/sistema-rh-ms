package com.rh.employee.repositories;

import com.rh.employee.models.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, UUID> {

    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);

}
