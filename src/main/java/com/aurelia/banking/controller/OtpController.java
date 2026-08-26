package com.aurelia.banking.controller;

import com.aurelia.banking.dto.AuthResponse;
import com.aurelia.banking.dto.OtpRequest;
import com.aurelia.banking.dto.OtpVerifyRequest;
import com.aurelia.banking.entity.RefreshToken;
import com.aurelia.banking.entity.User;
import com.aurelia.banking.repository.UserRepository;
import com.aurelia.banking.service.AuthService;
import com.aurelia.banking.service.OtpService;
import com.aurelia.banking.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/otp")
@RequiredArgsConstructor
public class OtpController {

    private final OtpService otpService;
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;

    @PostMapping("/request")
    public ResponseEntity<String> requestOtp(
            @RequestBody OtpRequest request) {

        otpService.generateOtp(
                request.phoneNumber(),
                request.purpose()
        );

        return ResponseEntity.ok(
                "OTP generated successfully"
        );
    }

    @PostMapping("/verify")
    public ResponseEntity<AuthResponse> verifyOtp(
            @RequestBody OtpVerifyRequest request) {

        // 1. Verify OTP
        otpService.verifyOtp(
                request.phoneNumber(),
                request.otp(),
                request.purpose()
        );

        // 2. Find user by phone number
        User user = userRepository
                .findByPhoneNumber(request.phoneNumber())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        // 3. Generate access token
        String accessToken =
                authService.generateToken(
                        user.getEmail()
                );

        // 4. Generate refresh token
        RefreshToken refreshToken =
                refreshTokenService.createRefreshToken(user);

        // 5. Return tokens
        return ResponseEntity.ok(  //So we didn't create new token logic. We simply moved the existing
                                     // token-generation step from /auth/login to /auth/otp/verify
                new AuthResponse(
                        accessToken,
                        refreshToken.getToken()
                )
        );
    }
}