package com.aurelia.banking.service;

import com.aurelia.banking.dto.TransferRequest;
import com.aurelia.banking.entity.Account;
import com.aurelia.banking.entity.IdempotencyKey;
import com.aurelia.banking.entity.Transaction;
import com.aurelia.banking.exception.AccountNotFoundException;
import com.aurelia.banking.exception.InsufficientBalanceException;
import com.aurelia.banking.repository.AccountRepository;
import com.aurelia.banking.repository.IdempotencyKeyRepository;
import com.aurelia.banking.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final IdempotencyKeyRepository idempotencyRepo;

    @Transactional
    public String transfer(TransferRequest request, String id) {

        // 🔴 STEP 1 — Check if key already exists
        Optional<IdempotencyKey> existing =
                idempotencyRepo.findByIdempotencyKey(id);

        if (existing.isPresent()) {
            return "Duplicate request ignored (idempotent)";
        }

        // 🔴 STEP 2 — Process transfer normally

        Account from = accountRepository.findById(request.getFrom())
                .orElseThrow(() -> new RuntimeException("From account not found"));

        Account to = accountRepository.findById(request.getTo())
                .orElseThrow(() -> new RuntimeException("To account not found"));

        if (from.getBalance().compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        from.setBalance(from.getBalance().subtract(request.getAmount()));
        to.setBalance(to.getBalance().add(request.getAmount()));

        accountRepository.save(from);
        accountRepository.save(to);

        String now = LocalDateTime.now().toString();

        transactionRepository.save(Transaction.builder()
                .accountId(from.getId())
                .type("debit")
                .amount(request.getAmount())
                .category("transfer")
                .createdAt(now)
                .build());

        transactionRepository.save(Transaction.builder()
                .accountId(to.getId())
                .type("credit")
                .amount(request.getAmount())
                .category("transfer")
                .createdAt(now)
                .build());

        // 🔴 STEP 3 — Save idempotency key
        idempotencyRepo.save(IdempotencyKey.builder()
                .idempotencyKey(id)
                .operation("TRANSFER")
                .response("SUCCESS")
                .createdAt(LocalDateTime.parse(now))
                .build());

        return "Transfer successful";
    }
}