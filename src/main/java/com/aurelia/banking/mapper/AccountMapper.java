package com.aurelia.banking.mapper;

import com.aurelia.banking.dto.AccountDTO;
import com.aurelia.banking.entity.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public AccountDTO toDTO(Account account) {
        return AccountDTO.builder()
                .id(account.getId())
                .nickname(account.getNickname())
                .balance(account.getBalance())
                .customerId(account.getCustomerId())
                .createdAt(account.getCreatedAt())
                .build();
    }

    public Account toEntity(AccountDTO dto) {
        return Account.builder()
                .id(dto.getId())
                .nickname(dto.getNickname())
                .balance(dto.getBalance())
                .customerId(dto.getCustomerId())
                .createdAt(dto.getCreatedAt())
                .build();
    }
}