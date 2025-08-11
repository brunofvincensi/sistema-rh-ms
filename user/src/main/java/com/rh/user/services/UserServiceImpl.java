package com.rh.user.services;

import com.rh.common.exceptions.BusinessException;
import com.rh.common.exceptions.ResourceNotFoundException;
import com.rh.user.dtos.UserRequest;
import com.rh.user.dtos.UserResponse;
import com.rh.user.dtos.UserUpdateRequest;
import com.rh.user.models.RoleConstants;
import com.rh.user.models.UserEntity;
import com.rh.user.producers.UserUpdateProducer;
import com.rh.user.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserUpdateProducer userUpdateProducer;

    public UserServiceImpl(UserRepository userRepository, UserUpdateProducer userUpdateProducer) {
        this.userRepository = userRepository;
        this.userUpdateProducer = userUpdateProducer;
    }

    @Override
    public void create(UserRequest userRequest) {
        if (userRequest == null) {
            return;
        }

        var user = new UserEntity();
        user.setCpf(userRequest.cpf());
        user.setEmployeeId(userRequest.employeeId());
        user.setRole(userRequest.role());

        String passwordEncoded = encodePassword(userRequest.password());
        user.setPassword(passwordEncoded);

        internalSave(user);
    }

    private static String encodePassword(String password) {
        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
        return bcrypt.encode(password);
    }

    @Override
    public void update(UserUpdateRequest userRequest) {
        if (userRequest == null || userRequest.id() == null) {
            return;
        }
        var user = userRepository.findById(userRequest.id()).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        user.setCpf(userRequest.cpf());
        // Apenas atualiza a role foi informada
        if (userRequest.role() != null) {
            user.setRole(userRequest.role());
        }

        UserEntity userUpdated = internalSave(user);

        // Depois de alterar com sucesso, publica na fila a alteração
        userUpdateProducer.publishUserUpdate(userUpdated);
    }

    @Override
    public UserResponse findById(UUID userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        return new UserResponse(user.getId(), user.getEmployeeId(),user.getRole());
    }

    private UserEntity internalSave(UserEntity user) {
        validateUniqueFields(user);
        if (user.getRole() == null) {
            user.setRole(RoleConstants.COLABORADOR);
        }
        return userRepository.save(user);
    }

    private void validateUniqueFields(UserEntity user)  {
        if (userRepository.existsByCpf(user.getCpf())) {
            throw new BusinessException("CPF já cadastrado");
        }
    }

}
