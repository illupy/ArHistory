package org.illupy.service.impl;

import lombok.RequiredArgsConstructor;
import org.illupy.config.JwtUtil;
import org.illupy.dto.*;
import org.illupy.entity.User;
import org.illupy.enums.UserRole;
import org.illupy.exception.ResourceNotFoundException;
import org.illupy.repository.UserRepository;
import org.illupy.service.AuthService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid email or password"));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new ResourceNotFoundException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(user);

        return LoginResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .role(user.getRole().name())
                .token(token)
                .build();
    }

    @Override
    public LoginResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email đã tồn tại");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .role(UserRole.TEACHER)
                .createdAt(LocalDateTime.now())
                .build();

        user = userRepository.save(user);

        String token = jwtUtil.generateToken(user);

        return LoginResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .role(user.getRole().name())
                .token(token)
                .build();
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> UserResponse.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .role(user.getRole().name())
                        .createdAt(user.getCreatedAt())
                        .build())
                .toList();
    }
}
