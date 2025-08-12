package com.rh.employee.controllers;

import com.rh.employee.dtos.WorkScheduleRequest;
import com.rh.employee.dtos.WorkScheduleResponse;
import com.rh.employee.models.WorkScheduleEntity;
import com.rh.employee.repositories.WorkScheduleRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/work-schedules")
public class WorkScheduleController {

    private final WorkScheduleRepository repository;

    public WorkScheduleController(WorkScheduleRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid WorkScheduleRequest workScheduleRequest) {
        WorkScheduleEntity workSchedule = new WorkScheduleEntity();
        workSchedule.setName(workScheduleRequest.name());
        workSchedule.setDays(workScheduleRequest.days());
        workSchedule.setHours(workScheduleRequest.hours());

        WorkScheduleEntity save = repository.save(workSchedule);

        return ResponseEntity.status(HttpStatus.CREATED).body(new WorkScheduleResponse(save));
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        List<WorkScheduleResponse> list = repository.findAll().stream().map(WorkScheduleResponse::new).toList();
        return ResponseEntity.ok(list);
    }

}
