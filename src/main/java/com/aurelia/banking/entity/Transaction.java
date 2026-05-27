package com.aurelia.banking.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name="transactions")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String accountId;

    private String loanId;

    private String type; // credit / debit

    private BigDecimal amount;

    private String category; // transfer, loan-payment, etc

    private String createdAt;
}
