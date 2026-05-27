package com.aurelia.banking.controller;

import com.aurelia.banking.dto.TransferRequest;
import com.aurelia.banking.service.TransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    // ✅ TRANSFER MONEY
    @PostMapping
    public ResponseEntity<String> transfer(
            @RequestHeader("Idempotency-Key") String key,
            @Valid @RequestBody TransferRequest request) {

        String result = transferService.transfer(request, key);

        return ResponseEntity.ok(result);
    }
}