package com.stock.inventorymanagement.controllers;

import com.stock.inventorymanagement.dto.SalesPersonDTO;
import com.stock.inventorymanagement.service.CustomerSalesPersonService;
import com.stock.inventorymanagement.service.SalesPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/salespersons")
public class SalesPersonController {

    private final SalesPersonService salesPersonService;

    @Autowired
    private CustomerSalesPersonService customerSalesPersonService;

    @Autowired
    public SalesPersonController(SalesPersonService salesPersonService) {
        this.salesPersonService = salesPersonService;
    }

    @PostMapping
    public ResponseEntity<SalesPersonDTO> createSalesPerson(@RequestBody SalesPersonDTO salesPersonDto) {
        SalesPersonDTO savedSalesPersonDto = salesPersonService.saveSalesPerson(salesPersonDto);
        return new ResponseEntity<>(savedSalesPersonDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalesPersonDTO> getSalesPersonById(@PathVariable Long id) {
        return salesPersonService.findSalesPersonById(id)
                .map(salesPersonDto -> new ResponseEntity<>(salesPersonDto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<SalesPersonDTO> getSalesPersonByEmail(@PathVariable String email) {
        return salesPersonService.findSalesPersonByEmail(email)
                .map(salesPersonDto -> new ResponseEntity<>(salesPersonDto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<SalesPersonDTO>> getAllSalesPersons() {
        List<SalesPersonDTO> salesPersonDtos = salesPersonService.findAllSalesPersons();
        return new ResponseEntity<>(salesPersonDtos, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SalesPersonDTO> updateSalesPerson(@PathVariable Long id, @RequestBody SalesPersonDTO salesPersonDto) {
        try {
            SalesPersonDTO updatedSalesPersonDto = salesPersonService.updateSalesPerson(id, salesPersonDto);
            return new ResponseEntity<>(updatedSalesPersonDto, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSalesPerson(@PathVariable Long id) {
        salesPersonService.deleteSalesPersonById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{salesPersonId}/customers")
    public ResponseEntity<Map<Long, Long>> getCustomerUserIdsBySalesPersonId(@PathVariable Long salesPersonId) {
        Map<Long, Long> customerUserIds = customerSalesPersonService.findCustomerIdsBySalesPersonId(salesPersonId);
        return ResponseEntity.ok(customerUserIds);
    }
}
