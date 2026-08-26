package com.aurelia.banking.dto;

public record OtpRequest(
        String phoneNumber,
        String purpose
) {
}