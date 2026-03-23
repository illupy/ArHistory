package org.illupy.service;

import org.illupy.dto.LoginRequest;
import org.illupy.dto.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest loginRequest);
}
