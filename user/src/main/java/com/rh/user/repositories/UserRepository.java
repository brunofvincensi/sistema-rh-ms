package com.rh.user.repositories;

import com.rh.user.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID>  {

    boolean existsByCpfAndIdNot(String cpf, UUID id);

    Optional<UserEntity> findByEmployeeId(UUID employeeId);

    boolean deleteByEmployeeId(UUID employeeId);

}
