package com.rh.time_clock.services;

import com.rh.time_clock.dtos.TimeEntryRequest;
import com.rh.time_clock.dtos.TimeEntryResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TimeEntryService {

    void record(TimeEntryRequest request);

    List<TimeEntryResponse> findByEmployeeAndDate(UUID employeeId, LocalDate date);

    List<TimeEntryResponse> findByMonthAndYear(UUID employeeId, Integer month, Integer year);

}
