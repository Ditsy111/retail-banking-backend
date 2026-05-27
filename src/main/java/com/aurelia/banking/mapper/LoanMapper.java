package com.aurelia.banking.mapper;

import com.aurelia.banking.dto.LoanDTO;
import com.aurelia.banking.entity.Loan;
import org.springframework.stereotype.Component;

@Component
public class LoanMapper {

    public LoanDTO toDTO(Loan loan) {
        return LoanDTO.builder()
                .id(loan.getId())
                .nickname(loan.getNickname())
                .principal(loan.getPrincipal())
                .outstanding(loan.getOutstanding())
                .apr(loan.getApr())
                .termMonths(loan.getTermMonths())
                .status(loan.getStatus())
                .customerId(loan.getCustomerId())
                .build();
    }

    public Loan toEntity(LoanDTO dto) {
        return Loan.builder()
                .id(dto.getId())
                .nickname(dto.getNickname())
                .principal(dto.getPrincipal())
                .outstanding(dto.getOutstanding())
                .apr(dto.getApr())
                .termMonths(dto.getTermMonths())
                .status(dto.getStatus())
                .customerId(dto.getCustomerId())
                .build();
    }
}
