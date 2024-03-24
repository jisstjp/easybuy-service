package com.stock.inventorymanagement.controllers;

import com.stock.inventorymanagement.service.CreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
@RestController
@RequestMapping("/api/v1/credits")
public class CreditController {

    @Autowired
    private CreditService creditService;

    @GetMapping("/total/{customerId}")
    public ResponseEntity<BigDecimal> getTotalCredits(@PathVariable Long customerId) {
        BigDecimal totalCredits = creditService.getTotalCredits(customerId);
        return ResponseEntity.ok(totalCredits);
    }
}
