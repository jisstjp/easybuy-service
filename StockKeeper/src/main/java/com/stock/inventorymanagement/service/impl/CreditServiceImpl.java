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
        updateCustomerStoreCredit(credit.getCustomer(), credit.getAmount(), true);
        return creditMapper.convertToDTO(credit);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public CreditDto updateCredit(Long id, CreditDto creditDto) {
        Credit existingCredit = creditRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Credit not found"));
        creditMapper.updateEntityFromDto(creditDto, existingCredit);
        existingCredit = creditRepository.save(existingCredit);
        updateCustomerStoreCredit(existingCredit.getCustomer(), creditDto.getAmount(), false);
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
}
