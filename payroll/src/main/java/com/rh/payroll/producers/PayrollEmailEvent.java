package com.rh.payroll.producers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayrollEmailEvent {

    private String emailTo;
    private String subject;
    private String text;

}
