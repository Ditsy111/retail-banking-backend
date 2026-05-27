package com.aurelia.banking.service;

import com.aurelia.banking.dto.AccountDTO;
import com.aurelia.banking.entity.Account;
import com.aurelia.banking.entity.Transaction;
import com.aurelia.banking.exception.AccountNotFoundException;
import com.aurelia.banking.exception.InsufficientBalanceException;
import com.aurelia.banking.mapper.AccountMapper;
import com.aurelia.banking.repository.AccountRepository;
import com.aurelia.banking.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final AccountMapper accountMapper;

    // ✅ CREATE ACCOUNT
    public AccountDTO createAccount(AccountDTO dto) {
        Account account = accountMapper.toEntity(dto);
        account.setCreatedAt(LocalDateTime.now().toString());
        Account saved = accountRepository.save(account);
        return accountMapper.toDTO(saved);
    }

    // ✅ GET ALL ACCOUNTS
    public List<AccountDTO> getAllAccounts() {
        return accountRepository.findAll()
                .stream()
                .map(accountMapper::toDTO)
                .toList();
    }

    // ✅ GET ACCOUNT BY ID
    public AccountDTO getAccountById(String id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with id " + id));

        return accountMapper.toDTO(account);
    }

    // ✅ DEPOSIT
    @Transactional
    public AccountDTO deposit(String id, BigDecimal amount) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with id " + id));

        account.setBalance(account.getBalance().add(amount));

        accountRepository.save(account);

        Transaction tx = Transaction.builder()
                .accountId(id)
                .type("credit")
                .amount(amount)
                .category("other")
                .createdAt(LocalDateTime.now().toString())
                .build();

        transactionRepository.save(tx);

        return accountMapper.toDTO(account);
    }

    // ✅ WITHDRAW
    @Transactional
    public AccountDTO withdraw(String id, BigDecimal amount) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with id " + id));

        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        account.setBalance(account.getBalance().subtract(amount));

        accountRepository.save(account);

        Transaction tx = Transaction.builder()
                .accountId(id)
                .type("debit")
                .amount(amount)
                .category("expenses")
                .createdAt(LocalDateTime.now().toString())
                .build();

        transactionRepository.save(tx);

        return accountMapper.toDTO(account);
    }
}