package com.aurelia.banking.controller;

import com.aurelia.banking.dto.AuthResponse;
import com.aurelia.banking.dto.LoginRequest;
import com.aurelia.banking.dto.RegisterRequest;
import com.aurelia.banking.entity.RefreshToken;
import com.aurelia.banking.entity.User;
import com.aurelia.banking.repository.UserRepository;
import com.aurelia.banking.service.AuthService;
import com.aurelia.banking.service.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.aurelia.banking.dto.RefreshTokenRequest;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @Valid @RequestBody RegisterRequest request
    ) {

        authService.register(request);

        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {

        // 1. AuthService checks email + password
        String accessToken =
                authService.login(request);

        // 2. Find the user who just logged in
        User user =
                userRepository
                        .findByEmail(request.getEmail())
                        .orElseThrow();


        // 3. Create and save refresh token
        RefreshToken refreshToken =
                refreshTokenService
                        .createRefreshToken(user);

        // 4. Return BOTH tokens
        return ResponseEntity.ok(
                new AuthResponse(
                        accessToken,
                        refreshToken.getToken()
                )
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(
            @RequestBody RefreshTokenRequest request
    ) {

        // 1. Find refresh token in database
        RefreshToken refreshToken =
                refreshTokenService.findByToken(
                        request.refreshToken()
                );

        // 2. Check expiration
        refreshTokenService.verifyExpiration(
                refreshToken
        );

        // 3. Get the user associated with this token
        User user = refreshToken.getUser();

        // 4. Generate a NEW access token
        String accessToken =
                authService.generateToken(
                        user.getEmail()
                );

        // 5. Return new access token
        return ResponseEntity.ok(
                new AuthResponse(
                        accessToken,
                        refreshToken.getToken()
                )
        );
    }
}