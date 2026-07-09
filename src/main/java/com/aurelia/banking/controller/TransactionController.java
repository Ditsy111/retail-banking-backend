package com.aurelia.banking.controller;

import com.aurelia.banking.entity.Transaction;
import com.aurelia.banking.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    // ✅ GET ALL TRANSACTIONS
    @GetMapping
    public List<Transaction> getAllTransactions(
            Authentication authentication) {

        return transactionService.getAllTransactions(
                authentication.getName());
    }

    // ✅ GET BY ACCOUNT
    @GetMapping("/account/{accountId}")
    public List<Transaction> getByAccount(@PathVariable String accountId) {
        return transactionService.getTransactionsByAccountId(accountId);
    }

    // ✅ GET TRANSACTIONS BY LOAN
    @GetMapping("/loan/{loanId}")
    public List<Transaction> getByLoan(@PathVariable String loanId) {
        return transactionService.getTransactionsByLoanId(loanId);
    }

    // ✅ GET SINGLE
    @GetMapping("/{id}")
    public Transaction getById(@PathVariable String id) {
        return transactionService.getTransactionById(id);
    }

    // (Optional admin)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        transactionService.deleteTransaction(id);
    }
}