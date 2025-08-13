package com.rh.payroll.services;

import com.rh.payroll.clients.time_clock.TimeEntryResponse;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Representa as horas consolidadas de um dia de um colaborador
 */
@Data
public class ConsolidatedWorkDay {

    private final LocalDate date;
    private final List<TimeEntryResponse> timeEntries;
    private final BigDecimal workedHours;

    public ConsolidatedWorkDay(LocalDate date, List<TimeEntryResponse> timeEntries) {
        this.date = date;
        this.timeEntries = new ArrayList<>(timeEntries);
        this.workedHours = calculateWorkedHours();
    }

    private BigDecimal calculateWorkedHours() {
        // Ordena marcações por horário
        timeEntries.sort(Comparator.comparing(TimeEntryResponse::getTime));

        BigDecimal totalHours = BigDecimal.ZERO;
        LocalTime checkIn = null;

        for (TimeEntryResponse timeEntry : timeEntries) {
            if (timeEntry.getType().equals("IN")) {
                checkIn = timeEntry.getTime();
            } else if (timeEntry.getType().equals("OUT") && checkIn != null) {
                Duration duration = Duration.between(checkIn, timeEntry.getTime());
                BigDecimal hours = new BigDecimal(duration.toMinutes())
                        .divide(new BigDecimal(60), 4, RoundingMode.HALF_UP);
                totalHours = totalHours.add(hours);
                checkIn = null; // Reset para próximo par
            }
        }

        return totalHours.setScale(4, RoundingMode.HALF_UP);
    }

    public boolean existsWorkedHours() {
        return workedHours != null && workedHours.compareTo(BigDecimal.ZERO) > 0;
    }

}
