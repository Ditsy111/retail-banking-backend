package com.aurelia.banking.service;

import com.aurelia.banking.entity.Otp;
import com.aurelia.banking.repository.OtpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final OtpRepository otpRepository;

    private final SecureRandom secureRandom =
            new SecureRandom();


    @Transactional
    public String generateOtp(
            String phoneNumber,
            String purpose
    ) {

        // Remove previous OTP
        otpRepository.deleteByPhoneNumberAndPurpose(
                phoneNumber,
                purpose
        );

        // Generate 6 digit OTP
        String otp = String.format(
                "%06d",
                secureRandom.nextInt(1_000_000)
        );

        Otp otpEntity = Otp.builder()
                .phoneNumber(phoneNumber)
                .otp(otp)
                .purpose(purpose)
                .expiryDate(
                        Instant.now().plusSeconds(5 * 60)
                )
                .build();

        otpRepository.save(otpEntity);

        System.out.println(
                "OTP for " + phoneNumber + ": " + otp
        );

        return otp;
    }


    @Transactional
    public void verifyOtp(
            String phoneNumber,
            String otp,
            String purpose
    ) {

        Otp otpEntity =
                otpRepository
                        .findByPhoneNumberAndOtpAndPurpose(
                                phoneNumber,
                                otp,
                                purpose
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Invalid OTP"
                                )
                        );


        if (otpEntity.getExpiryDate()
                .isBefore(Instant.now())) {

            otpRepository.delete(otpEntity);

            throw new RuntimeException(
                    "OTP expired"
            );
        }

        // OTP can only be used once
        otpRepository.delete(otpEntity);
    }
}