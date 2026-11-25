package com.aurelia.banking.service;

import com.aurelia.banking.dto.LoginRequest;
import com.aurelia.banking.dto.RegisterRequest;
import com.aurelia.banking.entity.Role;
import com.aurelia.banking.entity.User;
import com.aurelia.banking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final OtpService otpService;

    public void register(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName((request.getLastName()))
                .phoneNumber(request.getPhoneNumber())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.CUSTOMER)
                .build();

        userRepository.save(user);
    }

    public void registerAdmin(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        User user = User.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ADMIN)
                .build();
        userRepository.save(user);
    }

    public String login(LoginRequest request) {

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("Invalid email address"));

        boolean passwordMatches =
                passwordEncoder.matches(
                        request.getPassword(),
                        user.getPassword()
                );

        if (!passwordMatches) {
            throw new RuntimeException("Password Invalid");
        }

        otpService.generateOtp(
                user.getPhoneNumber(),
                "LOGIN"
        );

        return user.getPhoneNumber();
    }

    public void requestPasswordReset(String phoneNumber) {

        userRepository
                .findByPhoneNumber(phoneNumber)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        otpService.generateOtp(
                phoneNumber,
                "PASSWORD_RESET"
        );
    }

    public void resetPassword(
            String phoneNumber,
            String otp,
            String newPassword
    ) {

        // Verify the OTP
        otpService.verifyOtp(
                phoneNumber,
                otp,
                "PASSWORD_RESET"
        );

        // Find the user
        User user = userRepository
                .findByPhoneNumber(phoneNumber)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        // Encode and save new password
        user.setPassword(
                passwordEncoder.encode(newPassword)
        );

        userRepository.save(user);
    }



    public String generateToken(String email) {

        return jwtService.generateToken(email);
    }
}