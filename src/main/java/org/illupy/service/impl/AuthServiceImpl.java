package org.illupy.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.illupy.config.JwtUtil;
import org.illupy.dto.*;
import org.illupy.entity.User;
import org.illupy.enums.UserRole;
import org.illupy.exception.ResourceNotFoundException;
import org.illupy.repository.UserRepository;
import org.illupy.service.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    @Value("${app.frontend-url:http://localhost:5173}")
    private String frontendUrl;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Email hoặc mật khẩu không đúng"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResourceNotFoundException("Email hoặc mật khẩu không đúng");
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

        String rawPassword = request.getPassword();
        String hashedPassword = passwordEncoder.encode(rawPassword);

        User user = User.builder()
                .email(request.getEmail())
                .password(hashedPassword)
                .role(UserRole.TEACHER)
                .createdAt(LocalDateTime.now())
                .build();

        user = userRepository.save(user);

        // Send welcome email with password
        sendWelcomeEmail(request.getEmail(), rawPassword);

        String token = jwtUtil.generateToken(user);

        return LoginResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .role(user.getRole().name())
                .token(token)
                .build();
    }

    @Override
    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Email không tồn tại trong hệ thống"));

        String resetToken = UUID.randomUUID().toString();
        user.setResetToken(resetToken);
        user.setResetTokenExpiry(LocalDateTime.now().plusHours(1));
        userRepository.save(user);

        String resetLink = frontendUrl + "/reset-password?token=" + resetToken;
        sendResetEmail(email, resetLink);
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Token không hợp lệ"));

        if (user.getResetTokenExpiry() == null || user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token đã hết hạn. Vui lòng yêu cầu lại.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);
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

    @Override
    public List<UserResponse> getUsersForCurrentUser(Authentication authentication) {
        if (authentication == null) {
            return List.of();
        }
        String email = authentication.getName();
        User currentUser = userRepository.findByEmail(email).orElse(null);
        if (currentUser == null) {
            return List.of();
        }

        // Admin sees all users
        if (currentUser.getRole() == UserRole.ADMIN) {
            return getAllUsers();
        }

        // Teacher sees only themselves
        return List.of(UserResponse.builder()
                .id(currentUser.getId())
                .email(currentUser.getEmail())
                .role(currentUser.getRole().name())
                .createdAt(currentUser.getCreatedAt())
                .build());
    }

    private void sendWelcomeEmail(String toEmail, String password) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("[Histar] Tài khoản của bạn đã được tạo");
            message.setText(
                    "Xin chào,\n\n" +
                    "Tài khoản Histar Portal của bạn đã được tạo thành công.\n\n" +
                    "Thông tin đăng nhập:\n" +
                    "- Email: " + toEmail + "\n" +
                    "- Mật khẩu: " + password + "\n\n" +
                    "Vui lòng đăng nhập và đổi mật khẩu nếu cần.\n\n" +
                    "Trân trọng,\nHistar Team"
            );
            mailSender.send(message);
            log.info("Welcome email sent to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send welcome email to {}: {}", toEmail, e.getMessage());
        }
    }

    private void sendResetEmail(String toEmail, String resetLink) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("[Histar] Đặt lại mật khẩu");
            message.setText(
                    "Xin chào,\n\n" +
                    "Bạn đã yêu cầu đặt lại mật khẩu cho tài khoản Histar.\n\n" +
                    "Nhấn vào link sau để đặt mật khẩu mới (có hiệu lực trong 1 giờ):\n" +
                    resetLink + "\n\n" +
                    "Nếu bạn không yêu cầu, vui lòng bỏ qua email này.\n\n" +
                    "Trân trọng,\nHistar Team"
            );
            mailSender.send(message);
            log.info("Reset email sent to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send reset email to {}: {}", toEmail, e.getMessage());
        }
    }
}
