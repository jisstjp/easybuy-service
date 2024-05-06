package com.stock.inventorymanagement.controllers;

import com.stock.inventorymanagement.dto.CustomerSalesPersonDTO;
import com.stock.inventorymanagement.service.CustomerSalesPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/v1/relationships")
public class RelationshipController {

    @Autowired
    private CustomerSalesPersonService customerSalesPersonService;

    @PostMapping
    public ResponseEntity<CustomerSalesPersonDTO> createRelationship(@RequestBody CustomerSalesPersonDTO relationshipDto) {
        CustomerSalesPersonDTO newRelationship = customerSalesPersonService.createRelationship(relationshipDto);
        return ResponseEntity.ok(newRelationship);
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRelationship(@PathVariable Long id) {
        customerSalesPersonService.deleteRelationship(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<CustomerSalesPersonDTO>> getRelationshipsForCustomer(@PathVariable Long customerId) {
        List<CustomerSalesPersonDTO> relationships = customerSalesPersonService.findAllRelationshipsByCustomerId(customerId);
        return ResponseEntity.ok(relationships);
    }

    @GetMapping("/salesperson/{salesPersonId}")
    public ResponseEntity<List<CustomerSalesPersonDTO>> getRelationshipsForSalesPerson(@PathVariable Long salesPersonId) {
        List<CustomerSalesPersonDTO> relationships = customerSalesPersonService.findAllRelationshipsBySalesPersonId(salesPersonId);
        return ResponseEntity.ok(relationships);
    }


    @PostMapping("/salesperson/assign-customers")
    public ResponseEntity<List<CustomerSalesPersonDTO>> createMultipleRelationships(@RequestBody CustomerSalesPersonDTO relationshipDto) {
        List<CustomerSalesPersonDTO> newRelationships = customerSalesPersonService.createMultipleRelationships(relationshipDto);
        return ResponseEntity.ok(newRelationships);
    }

    @PutMapping("/salesperson/assign-customers")
    public ResponseEntity<List<CustomerSalesPersonDTO>> updateMultipleRelationships(@RequestBody CustomerSalesPersonDTO relationshipDto) {
        List<CustomerSalesPersonDTO> updatedRelationships = customerSalesPersonService.createOrUpdateRelationships(relationshipDto);
        return ResponseEntity.ok(updatedRelationships);
    }


}
