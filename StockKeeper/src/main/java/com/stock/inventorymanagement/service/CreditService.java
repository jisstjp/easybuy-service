package com.stock.inventorymanagement.service;

import com.stock.inventorymanagement.dto.CreditDto;

import java.math.BigDecimal;
import java.util.List;

public interface CreditService {
    CreditDto createCredit(CreditDto creditDTO);
    CreditDto updateCredit(Long id, CreditDto creditDTO);
    List<CreditDto> getAllCredits();
    CreditDto getCreditById(Long id);
    void deleteCredit(Long id);
    public BigDecimal getTotalCredits(Long customerId);
    public void subtractCredit(Long customerId, BigDecimal amount);

}
