package com.aurelia.banking.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="loans")
@Builder
@Entity
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String nickname;

    private BigDecimal principal;

    private BigDecimal outstanding;

    private BigDecimal apr;

    private Integer termMonths;

    private String status;

    private String customerId;
}

