package org.illupy.service;

import org.illupy.dto.*;

import java.util.List;

public interface AuthService {
    LoginResponse login(LoginRequest loginRequest);
    LoginResponse register(RegisterRequest registerRequest);
    List<UserResponse> getAllUsers();
}
