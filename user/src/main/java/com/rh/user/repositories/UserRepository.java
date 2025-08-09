package com.rh.user.repositories;

import com.rh.user.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID>  {

    boolean existsByEmployeeId(UUID uuid);

    boolean existsByCpf(String cpf);

}
