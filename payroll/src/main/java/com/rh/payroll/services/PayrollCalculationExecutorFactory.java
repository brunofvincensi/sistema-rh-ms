package com.rh.payroll.services;

public class PayrollCalculationExecutorFactory {

    public static PayrollCalculationExecutor getInstance() {
        return new PayrollCalculationExecutorImpl();
    }

}
