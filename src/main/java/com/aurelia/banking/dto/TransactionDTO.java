package com.aurelia.banking.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDTO {

    private String id;

    @NotBlank(message = "Account ID is required")
    private String accountId;

    @NotBlank(message = "Loan ID is required")
    private String loadId;

    @NotBlank(message = "Transaction type is required")
    private String type;

    @NotNull(message = "Transaction amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0")
    private BigDecimal amount;

    @NotBlank(message = "Transaction category is required")
    private String category;

    private String createdAt;
}