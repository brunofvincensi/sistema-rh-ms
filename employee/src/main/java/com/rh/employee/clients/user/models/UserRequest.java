package com.rh.employee.clients.user.models;

import java.util.UUID;

public record UserRequest (UUID employeeId, String cpf, String password, String role) {
}
