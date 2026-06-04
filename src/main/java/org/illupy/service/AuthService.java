package org.illupy.service;

import org.illupy.dto.*;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface AuthService {
    LoginResponse login(LoginRequest loginRequest);
    LoginResponse register(RegisterRequest registerRequest);
    List<UserResponse> getAllUsers();
    List<UserResponse> getUsersForCurrentUser(Authentication authentication);
    void forgotPassword(String email);
    void resetPassword(String token, String newPassword);
}
