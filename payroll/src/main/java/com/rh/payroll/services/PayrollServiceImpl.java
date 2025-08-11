package com.rh.payroll.services;

import com.rh.common.exceptions.BusinessException;
import com.rh.common.exceptions.ResourceNotFoundException;
import com.rh.payroll.domain.enums.PayrollRecordStatus;
import com.rh.payroll.domain.models.PayrollRecordEntity;
import com.rh.payroll.dtos.PayrollRecordInfoResponse;
import com.rh.payroll.dtos.PayrollRecordRequest;
import com.rh.payroll.producers.PayrollRecordProducer;
import com.rh.payroll.repositories.PayrollRecordRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PayrollServiceImpl implements PayrollService {

    private final PayrollRecordRepository payrollRecordRepository;
    private final PayrollRecordProducer payrollRecordProducer;

    public PayrollServiceImpl(PayrollRecordRepository payrollRecordRepository, PayrollRecordProducer payrollRecordProducer) {
        this.payrollRecordRepository = payrollRecordRepository;
        this.payrollRecordProducer = payrollRecordProducer;
    }

    @Override
    public UUID record(PayrollRecordRequest payrollRecordRequest) {
        if (payrollRecordRequest == null) {
            return null;
        }

        Optional<PayrollRecordEntity> payrollRecord = payrollRecordRepository.findByEmployeeIdAndReferenceMonthAndReferenceYearAndStatusIn(
                payrollRecordRequest.employeeId(),
                payrollRecordRequest.month(),
                payrollRecordRequest.year(),
                List.of(PayrollRecordStatus.PROCESSING, PayrollRecordStatus.COMPLETED));

        if (payrollRecord.isPresent()) {
            PayrollRecordEntity entity = payrollRecord.get();

            if (PayrollRecordStatus.COMPLETED.equals(entity.getStatus())) {
                throw new BusinessException("Já existe uma folha de pagamento para este período");
            }
            return entity.getId();
        }

        PayrollRecordEntity entity = newPayrollRecord(payrollRecordRequest);

        entity = payrollRecordRepository.save(entity);

        payrollRecordProducer.publishRecord(entity);

        return entity.getId();
    }

    private static PayrollRecordEntity newPayrollRecord(PayrollRecordRequest payrollRecordRequest) {
        PayrollRecordEntity entity = new PayrollRecordEntity();
        entity.setCreatedAt(LocalDate.now());
        entity.setMonth(payrollRecordRequest.month());
        entity.setYear(payrollRecordRequest.year());
        entity.setStatus(PayrollRecordStatus.PROCESSING);
        return entity;
    }

    @Override
    public PayrollRecordInfoResponse findById(UUID userId) {
        PayrollRecordEntity entity = payrollRecordRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Folha de pagamento não encontrada"));
        return new PayrollRecordInfoResponse(entity);
    }

}
