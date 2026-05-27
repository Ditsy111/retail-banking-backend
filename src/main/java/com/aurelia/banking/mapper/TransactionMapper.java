package com.aurelia.banking.mapper;

import com.aurelia.banking.dto.TransactionDTO;
import com.aurelia.banking.entity.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public TransactionDTO toDTO(Transaction tx) {
        return TransactionDTO.builder()
                .id(tx.getId())
                .accountId(tx.getAccountId())
                .loadId(tx.getId())
                .type(tx.getType())
                .amount(tx.getAmount())
                .category(tx.getCategory())
                .createdAt(tx.getCreatedAt())
                .build();
    }

    public Transaction toEntity(TransactionDTO dto) {
        return Transaction.builder()
                .id(dto.getId())
                .accountId(dto.getAccountId())
                .loanId(dto.getId())
                .type(dto.getType())
                .amount(dto.getAmount())
                .category(dto.getCategory())
                .createdAt(dto.getCreatedAt())
                .build();
    }
}