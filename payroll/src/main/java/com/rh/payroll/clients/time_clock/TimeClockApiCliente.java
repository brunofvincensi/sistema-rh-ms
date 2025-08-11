package com.rh.payroll.clients.time_clock;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "time-clock",
        url = "${time-clock.api.url}"
)
public interface TimeClockApiCliente {

    @GetMapping("/time-entries")
    public ResponseEntity<?> findTimeEntriesByMonthAndYear(@RequestParam @NotBlank String employeeIdParam,
                                                           @RequestParam @NotNull Integer month,
                                                           @RequestParam @NotNull Integer year);

}
