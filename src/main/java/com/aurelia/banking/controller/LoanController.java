package com.aurelia.banking.controller;

import com.aurelia.banking.dto.LoanDTO;
import com.aurelia.banking.dto.LoanPaymentRequest;
import com.aurelia.banking.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    // ✅ GET ALL LOANS
    @GetMapping
    public List<LoanDTO> getAllLoans() {
        return loanService.getAllLoans();
    }

    // ✅ CREATE LOAN
    @PostMapping("/{accountId}")
    public LoanDTO createLoan(@PathVariable String accountId,
                              @RequestBody LoanDTO dto) {
        return loanService.createLoan(dto, accountId);
    }

    // ✅ PAY LOAN
    @PostMapping("/payment")
    public ResponseEntity<String> payLoan(@RequestBody LoanPaymentRequest request) {
        loanService.payLoan(request);
        return ResponseEntity.ok("Loan payment successful");
    }
}
