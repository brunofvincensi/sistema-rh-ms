package com.rh.payroll.repositories;

import com.rh.payroll.domain.enums.PayrollRecordStatus;
import com.rh.payroll.domain.models.PayrollRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PayrollRecordRepository extends JpaRepository<PayrollRecordEntity, UUID>  {

    Optional<PayrollRecordEntity> findByEmployeeIdAndReferenceMonthAndReferenceYearAndStatusIn(
            UUID employeeId,
            Integer referenceMonth,
            Integer referenceYear,
            List<PayrollRecordStatus> statuses
    );

}
