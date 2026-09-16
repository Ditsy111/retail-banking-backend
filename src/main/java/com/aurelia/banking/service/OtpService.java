package com.aurelia.banking.service;

import com.aurelia.banking.entity.Otp;
import com.aurelia.banking.repository.OtpRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class OtpService {

    private final OtpRepository otpRepository;

    private final SecureRandom secureRandom = new SecureRandom();

    @Transactional
    public String generateOtp(String phoneNumber, String purpose) {

        // Delete any previous OTP for this phone number and purpose
        otpRepository.deleteByPhoneNumberAndPurpose(phoneNumber, purpose);

        // Generate a new 6-digit OTP
        String otp = String.format(
                "%06d",
                secureRandom.nextInt(1_000_000)
        );

        // Save OTP in database
        Otp otpEntity = Otp.builder()
                .phoneNumber(phoneNumber)
                .otp(otp)
                .purpose(purpose)
                .expiryDate(Instant.now().plusSeconds(5 * 60))
                .build();

        otpRepository.save(otpEntity);

        // DEVELOPMENT ONLY
        // Check the OTP in the IntelliJ console
        log.info("DEV OTP for {} [{}]: {}", phoneNumber, purpose, otp);

        return otp;
    }

    @Transactional
    public void verifyOtp(
            String phoneNumber,
            String otp,
            String purpose
    ) {

        Otp otpEntity = otpRepository
                .findByPhoneNumberAndOtpAndPurpose(
                        phoneNumber,
                        otp,
                        purpose
                )
                .orElseThrow(() ->
                        new RuntimeException("Invalid OTP")
                );

        // Check expiry
        if (otpEntity.getExpiryDate().isBefore(Instant.now())) {

            otpRepository.delete(otpEntity);

            throw new RuntimeException("OTP expired");
        }

        // OTP is valid → delete it so it cannot be reused
        otpRepository.delete(otpEntity);
    }
}