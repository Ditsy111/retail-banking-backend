package com.aurelia.banking.service;

import com.aurelia.banking.entity.Account;
import com.aurelia.banking.entity.Transaction;
import com.aurelia.banking.entity.User;
import com.aurelia.banking.exception.AccountNotFoundException;
import com.aurelia.banking.repository.AccountRepository;
import com.aurelia.banking.repository.TransactionRepository;
import com.aurelia.banking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    // ✅ CREATE / SAVE TRANSACTION
    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    // ✅ GET ALL TRANSACTIONS
    public List<Transaction> getAllTransactions(String email) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        List<Account> accounts =
                accountRepository.findByCustomerId(
                        String.valueOf(user.getId()));

        List<String> accountIds =
                accounts.stream()
                        .map(Account::getId)
                        .toList();

        return transactionRepository
                .findByAccountIdIn(accountIds);
    }

    // ✅ GET TRANSACTIONS BY ACCOUNT
    public List<Transaction> getTransactionsByAccountId(String accountId) {
        return transactionRepository.findByAccountId(accountId);
    }

    public List<Transaction> getTransactionsByLoanId(String loanId){
        return transactionRepository.findByLoanId(loanId);
    }

    // ✅ GET SINGLE TRANSACTION
    public Transaction getTransactionById(String id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Transaction not found with id " + id));
    }

    // ✅ DELETE TRANSACTION (optional, mostly admin use)
    public void deleteTransaction(String id) {
        transactionRepository.deleteById(id);
    }
}
