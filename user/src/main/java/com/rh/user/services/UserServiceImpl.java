package com.rh.user.services;

import com.rh.common.exceptions.BusinessException;
import com.rh.common.exceptions.ResourceNotFoundException;
import com.rh.user.dtos.UserRequest;
import com.rh.user.dtos.UserUpdateRequest;
import com.rh.user.models.RoleConstants;
import com.rh.user.models.UserEntity;
import com.rh.user.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserEntity create(UserRequest userRequest) {
        if (userRequest == null) {
            return null;
        }

        var user = new UserEntity();
        user.setCpf(user.getCpf());
        user.setEmployeeId(userRequest.employeeId());
        user.setRole(userRequest.role());

        String passwordEncoded = encodePassword(userRequest.password());
        user.setPassword(passwordEncoded);

        return internalSave(user);
    }

    private static String encodePassword(String password) {
        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
        return bcrypt.encode(password);
    }

    @Override
    public UserEntity update(UserUpdateRequest userRequest) {
        if (userRequest == null || userRequest.id() == null) {
            return null;
        }
        var user = userRepository.findById(userRequest.id()).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        user.setCpf(user.getCpf());
        // Apenas atualiza a role foi informada
        if (userRequest.role() != null) {
            user.setRole(userRequest.role());
        }
        return internalSave(user);
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
