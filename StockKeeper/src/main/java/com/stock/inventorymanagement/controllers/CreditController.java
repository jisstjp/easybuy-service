package com.stock.inventorymanagement.controllers;

import com.stock.inventorymanagement.dto.CreditDto;
import com.stock.inventorymanagement.service.CreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

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

    @PostMapping
    public ResponseEntity<CreditDto> createCredit(@RequestBody CreditDto creditDto) {
        CreditDto newCredit = creditService.createCredit(creditDto);
        return ResponseEntity.ok(newCredit);
    }

    @GetMapping
    public ResponseEntity<List<CreditDto>> getAllCredits() {
        List<CreditDto> credits = creditService.getAllCredits();
        return ResponseEntity.ok(credits);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CreditDto> updateCredit(@PathVariable Long id, @RequestBody CreditDto creditDto) {
        CreditDto updatedCredit = creditService.updateCredit(id, creditDto);
        if (updatedCredit != null) {
            return ResponseEntity.ok(updatedCredit);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCredit(@PathVariable Long id) {
        try {
            creditService.deleteCredit(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }





}
