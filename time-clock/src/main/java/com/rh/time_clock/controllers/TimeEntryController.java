package com.rh.time_clock.controllers;

import com.rh.time_clock.dtos.TimeEntryRequest;
import com.rh.time_clock.dtos.TimeEntryResponse;
import com.rh.time_clock.services.TimeEntryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/time-entries")
public class TimeEntryController {

    private final TimeEntryService timeEntryService;
    private final HttpServletRequest httpServletRequest;

    public TimeEntryController(TimeEntryService timeEntryService, HttpServletRequest httpServletRequest) {
        this.timeEntryService = timeEntryService;
        this.httpServletRequest = httpServletRequest;
    }

    @PostMapping("/record")
    public ResponseEntity<?> record(@RequestBody @Valid TimeEntryRequest request) {
        String employeeIdHeader = httpServletRequest.getHeader("employeeId");
        UUID employeeId = UUID.fromString(employeeIdHeader);

        timeEntryService.record(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/daily")
    public ResponseEntity<?> findByEmployeeAndDate(@RequestParam @NotBlank String employeeIdParam,
                                                              @RequestParam @NotNull LocalDate date) {
        UUID employeeId = UUID.fromString(employeeIdParam);

        return findTimeEntriesByEmployeeAndDate(date, employeeId);
    }

    @GetMapping("/daily/me")
    public ResponseEntity<?> findMyTimeEntriesByDate(@RequestParam @NotNull LocalDate date) {
        String employeeIdHeader = httpServletRequest.getHeader("employeeId");
        UUID employeeId = UUID.fromString(employeeIdHeader);

        return findTimeEntriesByEmployeeAndDate(date, employeeId);
    }

    @GetMapping("/monthly")
    public ResponseEntity<?> findByMonthAndYear(@RequestParam @NotBlank String employeeIdParam,
                                                @RequestParam @NotNull Integer month,
                                                @RequestParam @NotNull Integer year) {
        UUID employeeId = UUID.fromString(employeeIdParam);

        List<TimeEntryResponse> timesEntry = timeEntryService.findByMonthAndYear(employeeId, month, year);
        return ResponseEntity.ok(timesEntry);
    }

    private ResponseEntity<?> findTimeEntriesByEmployeeAndDate(LocalDate date, UUID employeeId) {
        List<TimeEntryResponse> timesEntry = timeEntryService.findByEmployeeAndDate(employeeId, date);
        if (timesEntry.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(timesEntry);
    }

}
