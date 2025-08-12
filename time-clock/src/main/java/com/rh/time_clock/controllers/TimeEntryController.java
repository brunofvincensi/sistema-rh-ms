package com.rh.time_clock.controllers;

import com.rh.common.HeaderConstants;
import com.rh.common.exceptions.ResourceNotFoundException;
import com.rh.common.users.RoleConstants;
import com.rh.time_clock.domain.models.TimeEntryEntity;
import com.rh.time_clock.dtos.TimeEntryRequest;
import com.rh.time_clock.dtos.TimeEntryResponse;
import com.rh.time_clock.dtos.TimeEntryUpdateRequest;
import com.rh.time_clock.repositories.TimeEntryRepository;
import com.rh.time_clock.services.TimeEntryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/time-entries")
public class TimeEntryController {

    private final TimeEntryService timeEntryService;
    private final TimeEntryRepository timeEntryRepository;
    private final HttpServletRequest httpServletRequest;

    public TimeEntryController(TimeEntryService timeEntryService, TimeEntryRepository timeEntryRepository, HttpServletRequest httpServletRequest) {
        this.timeEntryService = timeEntryService;
        this.timeEntryRepository = timeEntryRepository;
        this.httpServletRequest = httpServletRequest;
    }

    @PostMapping("/record")
    public ResponseEntity<?> record(@RequestBody @Valid TimeEntryRequest request) {
        String employeeIdHeader = httpServletRequest.getHeader(HeaderConstants.EMPLOYEE_ID);
        UUID employeeId = UUID.fromString(employeeIdHeader);

        timeEntryService.record(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/record/{id}")
    public ResponseEntity<?> updateRecord(@PathVariable("id") String idTimeEntry,
                                          @RequestBody @Valid TimeEntryUpdateRequest request) {
        String employeeIdHeader = httpServletRequest.getHeader(HeaderConstants.EMPLOYEE_ID);
        String userRole = httpServletRequest.getHeader(HeaderConstants.USER_ROLE);
        UUID employeeId = UUID.fromString(employeeIdHeader);

        TimeEntryEntity timeEntryEntity = timeEntryRepository.findById(UUID.fromString(idTimeEntry)).orElseThrow(() -> new ResourceNotFoundException("Registro de hora não encontrado"));

        // Colaborador não pode alterar registro de ponto de outros colaboradores
        if (RoleConstants.COLABORADOR.equals(userRole)
                && !Objects.equals(employeeId, timeEntryEntity.getEmployeeId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        timeEntryService.updateRecord(timeEntryEntity, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/daily")
    public ResponseEntity<?> findByEmployeeAndDate(@RequestParam @NotBlank String employeeIdParam,
                                                              @RequestParam @NotNull LocalDate date) {
        UUID employeeId = UUID.fromString(employeeIdParam);

        return ResponseEntity.ok(timeEntryService.findByEmployeeAndDate(employeeId, date));
    }

    @GetMapping("/daily/me")
    public ResponseEntity<?> findMyTimeEntriesByDate(@RequestParam @NotNull LocalDate date) {
        String employeeIdHeader = httpServletRequest.getHeader("employeeId");
        UUID employeeId = UUID.fromString(employeeIdHeader);

        return ResponseEntity.ok(timeEntryService.findByEmployeeAndDate(employeeId, date));
    }

    @GetMapping("/monthly")
    public ResponseEntity<?> findByMonthAndYear(@RequestParam @NotBlank String employeeIdParam,
                                                @RequestParam @NotNull Integer month,
                                                @RequestParam @NotNull Integer year) {
        UUID employeeId = UUID.fromString(employeeIdParam);

        List<TimeEntryResponse> timesEntry = timeEntryService.findByMonthAndYear(employeeId, month, year);
        return ResponseEntity.ok(timesEntry);
    }

}
