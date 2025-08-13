package com.rh.payroll.services;

import com.rh.common.exceptions.BusinessException;
import com.rh.common.exceptions.ResourceNotFoundException;
import com.rh.payroll.domain.enums.PayrollRecordStatus;
import com.rh.payroll.domain.models.PayrollRecordEntity;
import com.rh.payroll.dtos.PayrollRecordInfoResponse;
import com.rh.payroll.dtos.PayrollRecordRequest;
import com.rh.payroll.dtos.PayrollResponse;
import com.rh.payroll.producers.PayrollRecordProducer;
import com.rh.payroll.repositories.PayrollRecordRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PayrollRecordServiceImpl implements PayrollRecordService {

    private final PayrollRecordRepository payrollRecordRepository;
    private final PayrollRecordProducer payrollRecordProducer;

    public PayrollRecordServiceImpl(PayrollRecordRepository payrollRecordRepository, PayrollRecordProducer payrollRecordProducer) {
        this.payrollRecordRepository = payrollRecordRepository;
        this.payrollRecordProducer = payrollRecordProducer;
    }

    @Override
    public UUID record(PayrollRecordRequest payrollRecordRequest, UUID actionUserId, UUID actionEmployeeId) {
        if (payrollRecordRequest == null) {
            return null;
        }

        Optional<PayrollRecordEntity> payrollRecord = payrollRecordRepository.findByEmployeeIdAndMonthAndYearAndStatusIn(
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

        PayrollRecordEntity entity = newPayrollRecord(payrollRecordRequest, actionUserId);

        entity = payrollRecordRepository.save(entity);

        payrollRecordProducer.publishRecord(entity, actionEmployeeId);

        return entity.getId();
    }

    private static PayrollRecordEntity newPayrollRecord(PayrollRecordRequest payrollRecordRequest, UUID actionUserId) {
        PayrollRecordEntity entity = new PayrollRecordEntity();
        entity.setEmployeeId(payrollRecordRequest.employeeId());
        entity.setCreatedAt(LocalDate.now());
        entity.setMonth(payrollRecordRequest.month());
        entity.setYear(payrollRecordRequest.year());
        entity.setStatus(PayrollRecordStatus.PROCESSING);
        entity.setActionUserId(actionUserId);
        return entity;
    }

    @Override
    public PayrollRecordInfoResponse findById(UUID userId) {
        PayrollRecordEntity entity = payrollRecordRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Folha de pagamento não encontrada"));
        return new PayrollRecordInfoResponse(entity);
    }

    @Override
    public List<PayrollRecordInfoResponse> findAll() {
        return payrollRecordRepository.findAll().stream().map(PayrollRecordInfoResponse::new).toList();
    }

    @Override
    public PayrollResponse findPayrollResult(UUID employeeId, Integer month, Integer year) {
        PayrollRecordEntity entity = payrollRecordRepository.findByEmployeeIdAndMonthAndYearAndStatusIn(employeeId, month, year, List.of(PayrollRecordStatus.COMPLETED))
                .orElseThrow(() -> new ResourceNotFoundException("Folha de pagamento não processada"));

        PayrollResponse response = new PayrollResponse();
        response.setBaseSalary(entity.getBaseSalary());
        response.setConsolidatedSalary(entity.getConsolidatedSalary());

        return response;
    }

}
