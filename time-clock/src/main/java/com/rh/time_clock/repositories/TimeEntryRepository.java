package com.rh.time_clock.repositories;

import com.rh.time_clock.domain.models.TimeEntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TimeEntryRepository extends JpaRepository<TimeEntryEntity, UUID>  {

    Optional<TimeEntryEntity> findFirstByEmployeeIdAndDateOrderByTimeDesc(UUID employeeId, LocalDate date);

    List<TimeEntryEntity> findEmployeeIdAndByDate(UUID employeeId, LocalDate date);

    List<TimeEntryEntity> findByDataBetween(LocalDate inicioMes, LocalDate fimMes);

}
