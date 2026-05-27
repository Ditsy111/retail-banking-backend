package com.aurelia.banking.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanDTO {

    private String id;

    @NotBlank(message = "Loan nickname is required")
    private String nickname;

    @NotNull(message = "Principal amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Principal must be greater than 0")
    private BigDecimal principal;

    @NotNull(message = "Outstanding amount is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Outstanding cannot be negative")
    private BigDecimal outstanding;

    @NotNull(message = "APR is required")
    @DecimalMin(value = "0.0", message = "APR cannot be negative")
    private BigDecimal apr;

    @NotNull(message = "Loan term is required")
    @Min(value = 1, message = "Loan term must be at least 1 month")
    private Integer termMonths;

    @NotBlank(message = "Loan status is required")
    private String status;

    @NotNull(message = "Principal amount is required")
    private String customerId;
}