package com.aurelia.banking.controller;

import com.aurelia.banking.dto.AdminUserDTO;
import com.aurelia.banking.entity.Transaction;
import com.aurelia.banking.service.TransactionService;
import com.aurelia.banking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final TransactionService transactionService;
    private final UserService userService;

    @GetMapping("/transactions")
    public List<Transaction> allTransactions() {
        return transactionService.getEveryTransaction();
    }

    @GetMapping("/users")
    public List<AdminUserDTO> users() {
        return userService.listUsers();
    }
}