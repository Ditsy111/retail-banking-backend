package com.aurelia.banking.dto;

public record PasswordResetVerifyRequest(
        String phoneNumber,
        String otp,
        String newPassword
) {
}