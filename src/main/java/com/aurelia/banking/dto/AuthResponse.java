package com.aurelia.banking.dto;

public record AuthResponse(
        String accessToken,
        String refreshToken
) {}