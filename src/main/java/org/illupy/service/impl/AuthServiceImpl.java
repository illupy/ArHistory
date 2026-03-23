package org.illupy.service.impl;

import lombok.RequiredArgsConstructor;
import org.illupy.dto.LoginRequest;
import org.illupy.dto.LoginResponse;
import org.illupy.entity.User;
import org.illupy.exception.ResourceNotFoundException;
import org.illupy.repository.UserRepository;
import org.illupy.service.AuthService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl  implements AuthService {
    private final UserRepository userRepository;

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid email or password"));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new ResourceNotFoundException("Invalid email or password");
        }

        return LoginResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }
}
