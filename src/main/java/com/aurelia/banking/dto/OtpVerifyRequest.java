package com.aurelia.banking.dto;

public record OtpVerifyRequest(
        String phoneNumber,
        String otp,
        String purpose
) {
}