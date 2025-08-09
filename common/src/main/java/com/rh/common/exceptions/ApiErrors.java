package com.rh.common.exceptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ApiErrors {

    private final List<String> errors;

    public ApiErrors(List<String> errors) {
        this.errors = errors;
    }

    public ApiErrors(String mensagemErro) {
        this.errors = new ArrayList<>(Collections.singletonList(mensagemErro));
    }

    public List<String> getErrors() {
        return errors;
    }

}