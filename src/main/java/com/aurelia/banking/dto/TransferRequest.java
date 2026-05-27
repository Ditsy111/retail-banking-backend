package com.aurelia.banking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferRequest {

    @NotBlank(message = "From account required")
    private String from;

    @NotBlank(message = "To account required")
    private String to;

    @NotNull(message = "Amount required")
    @Positive(message = "Amount must be greater than 0")
    private BigDecimal amount;
}