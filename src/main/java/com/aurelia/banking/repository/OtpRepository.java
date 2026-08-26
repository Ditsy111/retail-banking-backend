package com.aurelia.banking.repository;

import com.aurelia.banking.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OtpRepository
        extends JpaRepository<Otp, UUID> {

    Optional<Otp> findByPhoneNumberAndOtpAndPurpose(
            String phoneNumber,
            String otp,
            String purpose
    );

    void deleteByPhoneNumberAndPurpose(
            String phoneNumber,
            String purpose
    );
}