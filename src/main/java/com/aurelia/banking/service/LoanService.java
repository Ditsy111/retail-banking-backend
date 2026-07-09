package com.aurelia.banking.service;

import com.aurelia.banking.dto.LoanDTO;
import com.aurelia.banking.dto.LoanPaymentRequest;
import com.aurelia.banking.entity.Account;
import com.aurelia.banking.entity.Loan;
import com.aurelia.banking.entity.Transaction;
import com.aurelia.banking.entity.User;
import com.aurelia.banking.exception.AccountNotFoundException;
import com.aurelia.banking.exception.InsufficientBalanceException;
import com.aurelia.banking.exception.LoanNotFoundException;
import com.aurelia.banking.mapper.LoanMapper;
import com.aurelia.banking.repository.AccountRepository;
import com.aurelia.banking.repository.LoanRepository;
import com.aurelia.banking.repository.TransactionRepository;
import com.aurelia.banking.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final LoanMapper loanMapper;
    private final UserRepository userRepository;

    // ✅ GET ALL LOANS
    public List<LoanDTO> getAllLoans(String email) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return loanRepository
                .findByCustomerId(String.valueOf(user.getId()))
                .stream()
                .map(loanMapper::toDTO)
                .toList();
    }

    // ✅ CREATE LOAN (DISBURSEMENT)
    @Transactional
    public LoanDTO createLoan(LoanDTO dto, String accountId) {

        Loan loan = loanMapper.toEntity(dto);

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with id " + accountId));

        // credit account
        account.setBalance(account.getBalance().add(loan.getPrincipal()));

        accountRepository.save(account);
        loanRepository.save(loan);

        transactionRepository.save(Transaction.builder()
                .accountId(accountId)
                .type("credit")
                .amount(loan.getPrincipal())
                .category("loan")
                .createdAt(LocalDateTime.now().toString())
                .build());

        return loanMapper.toDTO(loan);
    }

    // ✅ LOAN PAYMENT
    @Transactional
    public void payLoan(LoanPaymentRequest request) {

        Account account = accountRepository.findById(request.getFromAccount())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        Loan loan = loanRepository.findById(request.getLoanId())
                .orElseThrow(() -> new LoanNotFoundException("Loan not found"));

        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        account.setBalance(account.getBalance().subtract(request.getAmount()));

        loan.setOutstanding(loan.getOutstanding().subtract(request.getAmount()));

        if (loan.getOutstanding().compareTo(BigDecimal.ZERO) == 0) {
            loan.setStatus("closed");
        }

        accountRepository.save(account);
        loanRepository.save(loan);

        transactionRepository.save(Transaction.builder()
                .accountId(account.getId())
                .type("debit")
                .amount(request.getAmount())
                .category("loan-payment")
                .createdAt(LocalDateTime.now().toString())
                .build());
    }
}