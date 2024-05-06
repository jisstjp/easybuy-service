package com.stock.inventorymanagement.service.impl;

import com.stock.inventorymanagement.domain.Credit;
import com.stock.inventorymanagement.domain.Customer;
import com.stock.inventorymanagement.dto.CreditDto;
import com.stock.inventorymanagement.mapper.CreditMapper;
import com.stock.inventorymanagement.repository.CreditRepository;
import com.stock.inventorymanagement.repository.CustomerRepository;
import com.stock.inventorymanagement.service.CreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CreditServiceImpl implements CreditService {

    @Autowired
    private CreditRepository creditRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CreditMapper creditMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public CreditDto createCredit(CreditDto creditDto) {
        Credit credit = creditMapper.convertToEntity(creditDto);
        credit = creditRepository.save(credit);
        return creditMapper.convertToDTO(credit);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public CreditDto updateCredit(Long id, CreditDto creditDto) {
        Credit existingCredit = creditRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Credit not found"));
        creditMapper.updateEntityFromDto(creditDto, existingCredit);
        existingCredit = creditRepository.save(existingCredit);
        return creditMapper.convertToDTO(existingCredit);
    }


    @Override
    public List<CreditDto> getAllCredits() {
        return creditRepository.findAll().stream()
                .map(creditMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CreditDto getCreditById(Long id) {
        Credit credit = creditRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Credit not found"));
        return creditMapper.convertToDTO(credit);
    }

    @Override
    public void deleteCredit(Long id) {

        creditRepository.deleteById(id);
    }

    private void updateCustomerStoreCredit(Customer customer, BigDecimal amountDifference, boolean isNewCredit) {
        if (!isNewCredit) {
            customer.addToStoreCredit(amountDifference);
        } else {
            customer.addToStoreCredit(amountDifference);
        }
        customerRepository.save(customer);
    }


    public BigDecimal getTotalCredits(Long customerId) {
        List<Credit> activeCredits = creditRepository.findByCustomerIdAndStatusAndExpiryDateAfter(customerId, "ACTIVE", new Date());
        BigDecimal totalCredits = BigDecimal.ZERO;
        for (Credit credit : activeCredits) {
            BigDecimal amount = credit.getAmount();
            if (amount != null) {
                totalCredits = totalCredits.add(amount);
            }
            // If amount is null, it will be skipped and not added to totalCredits
        }
        return totalCredits;
    }

    public void subtractCredit(Long customerId, BigDecimal amount) {
        List<Credit> credits = creditRepository.findByCustomerIdAndStatus(customerId, "ACTIVE");

        BigDecimal amountToSubtract = amount;
        for (Credit credit : credits) {
            if (amountToSubtract.compareTo(BigDecimal.ZERO) <= 0) {
                break;
            }

            BigDecimal creditAmount = credit.getAmount();
            if (creditAmount.compareTo(amountToSubtract) >= 0) {
                credit.setAmount(creditAmount.subtract(amountToSubtract));
                credit.setStatus(credit.getAmount().compareTo(BigDecimal.ZERO) == 0 ? "USED" : "ACTIVE");
                amountToSubtract = BigDecimal.ZERO;
            } else {
                amountToSubtract = amountToSubtract.subtract(creditAmount);
                credit.setAmount(BigDecimal.ZERO);
                credit.setStatus("USED");
            }
            creditRepository.save(credit);
        }

        if (amountToSubtract.compareTo(BigDecimal.ZERO) > 0) {
            // Handle the case where there isn't enough credit
            throw new RuntimeException("Insufficient credit available");
        }
    }

}
