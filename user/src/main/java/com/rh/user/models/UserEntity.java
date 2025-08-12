package com.rh.user.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

import java.util.UUID;

@Data
@Entity
@Table(name = "TB_USER")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    @Column(name = "employee_id", nullable = false, unique = true)
    private UUID employeeId;

    @CPF
    @Column(name = "ds_cpf", nullable = false, unique = true)
    private String cpf;

    @NotBlank
    @Column(name = "ds_password", nullable = false)
    private String password;

    @NotBlank
    @Column(name = "ds_role", nullable = false)
    private String role;

}
