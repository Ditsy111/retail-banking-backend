package com.aurelia.banking.controller;

import com.aurelia.banking.dto.AccountDTO;
import com.aurelia.banking.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    // ✅ CREATE ACCOUNT
    @PostMapping
    public AccountDTO createAccount(@Valid @RequestBody AccountDTO dto) {
        return accountService.createAccount(dto);
    }

    // ✅ GET ALL ACCOUNTS
    @GetMapping
    public List<AccountDTO> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    // ✅ GET ACCOUNT BY ID
    @GetMapping("/{id}")
    public AccountDTO getAccountById(@PathVariable String id) {
        return accountService.getAccountById(id);
    }

    // ✅ DEPOSIT
    @PostMapping("/{id}/deposit")
    public AccountDTO deposit(@PathVariable String id,
                              @RequestParam BigDecimal amount) {
        return accountService.deposit(id, amount);
    }

    // ✅ WITHDRAW
    @PostMapping("/{id}/withdraw")
    public AccountDTO withdraw(@PathVariable String id,
                               @RequestParam BigDecimal amount) {
        return accountService.withdraw(id, amount);
    }
}