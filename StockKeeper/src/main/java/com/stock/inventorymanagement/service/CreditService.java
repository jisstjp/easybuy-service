package com.stock.inventorymanagement.service;

import com.stock.inventorymanagement.dto.CreditDto;

import java.util.List;

public interface CreditService {
    CreditDto createCredit(CreditDto creditDTO);
    CreditDto updateCredit(Long id, CreditDto creditDTO);
    List<CreditDto> getAllCredits();
    CreditDto getCreditById(Long id);
    void deleteCredit(Long id);
}
