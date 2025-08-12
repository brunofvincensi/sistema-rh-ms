package com.rh.payroll.producers;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class PayrollEmailEvent {

    private String emailTo;
    private String subject;
    private String text;

}
