package com.rh.time_clock.services;

import com.rh.common.exceptions.BusinessException;
import com.rh.time_clock.domain.enums.TimeEntryType;
import com.rh.time_clock.dtos.TimeEntryRequest;
import com.rh.time_clock.domain.models.TimeEntryEntity;
import com.rh.time_clock.dtos.TimeEntryResponse;
import com.rh.time_clock.dtos.TimeEntryUpdateRequest;
import com.rh.time_clock.repositories.TimeEntryRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TimeEntryServiceImpl implements TimeEntryService {

    private final TimeEntryRepository timeEntryRepository;

    public TimeEntryServiceImpl(TimeEntryRepository timeEntryRepository) {
        this.timeEntryRepository = timeEntryRepository;
    }

    @Override
    public void record(TimeEntryRequest request) {
        TimeEntryEntity timeEntry = new TimeEntryEntity();

        timeEntry.setEmployeeId(request.employeeId());
        timeEntry.setObservation(request.observation());
        timeEntry.setDate(LocalDate.now());
        timeEntry.setTime(LocalTime.now());

        TimeEntryEntity lastEntry = findLastEntry(timeEntry);

        validateTimeEntry(timeEntry, lastEntry);

        TimeEntryType nextType = lastEntry == null ? TimeEntryType.IN : lastEntry.getType().reverse();
        timeEntry.setType(nextType);

        timeEntryRepository.save(timeEntry);
    }

    @Override
    public void updateRecord(TimeEntryEntity timeEntry, TimeEntryUpdateRequest request) {
        timeEntry.setDate(request.date());
        timeEntry.setTime(request.time());
        timeEntry.setObservation(request.observation());
        timeEntryRepository.save(timeEntry);
    }

    @Override
    public List<TimeEntryResponse> findByEmployeeAndDate(UUID employeeId, LocalDate date) {
        return timeEntryRepository
                .findByEmployeeIdAndDate(employeeId, date)
                .stream()
                .map(TimeEntryResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<TimeEntryResponse> findByMonthAndYear(UUID employeeId, Integer month, Integer year) {
        // Pega o primeiro e último dia do mês para utilizar o filtro between,
        // habilitando o uso do índice data, deixando a query mais performatica
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startMonth = yearMonth.atDay(1);
        LocalDate endMonth = yearMonth.atEndOfMonth();

        return timeEntryRepository.findByDateBetween(startMonth, endMonth)
                .stream()
                .map(TimeEntryResponse::new)
                .collect(Collectors.toList());
    }

    private TimeEntryEntity findLastEntry(TimeEntryEntity timeEntry) {
        return timeEntryRepository
                .findFirstByEmployeeIdAndDateOrderByTimeDesc(timeEntry.getEmployeeId(), timeEntry.getDate())
                .orElse(null);
    }

    private void validateTimeEntry(TimeEntryEntity newEntry, TimeEntryEntity lastEntry) {
        if (lastEntry == null) {
            return;
        }

        LocalTime currentTime = newEntry.getTime();
        if (currentTime.isBefore(lastEntry.getTime())) {
            throw new BusinessException("A marcação não pode ser anterior à última registrada.");
        }
        if (Duration.between(lastEntry.getTime(), currentTime).toMinutes() < 1) {
            throw new BusinessException("A marcação deve ter pelo menos 1 minuto de intervalo da última.");
        }
    }

}
