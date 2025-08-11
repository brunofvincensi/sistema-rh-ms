package com.rh.user.producers;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class UserUpdatedEvent {

    private UUID id;
    private UUID employeeId;
    private String cpf;
    private String role;

}
