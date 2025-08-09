package com.rh.employee.repositories;

import com.rh.employee.models.WorkScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WorkScheduleRepository extends JpaRepository<WorkScheduleEntity, UUID>  {
}
