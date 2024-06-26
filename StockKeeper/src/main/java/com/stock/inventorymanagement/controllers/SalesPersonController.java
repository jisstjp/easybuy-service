package com.stock.inventorymanagement.controllers;

import com.stock.inventorymanagement.dto.SalesPersonDTO;
import com.stock.inventorymanagement.dto.SalesPersonPublicDTO;
import com.stock.inventorymanagement.service.CustomerSalesPersonService;
import com.stock.inventorymanagement.service.SalesPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @GetMapping("/user/{userId}")
    public ResponseEntity<SalesPersonPublicDTO> getSalesPersonPublicByUserId(@PathVariable Long userId) {
        return salesPersonService.findSalesPersonByUserId(userId)
                .map(this::convertToPublicDTO) // Convert to public DTO
                .map(publicDto -> new ResponseEntity<>(publicDto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<SalesPersonPublicDTO>> getAllSalesPersons() {
        List<SalesPersonDTO> salesPersonDtos = salesPersonService.findAllSalesPersons();
        List<SalesPersonPublicDTO> publicDtos = salesPersonDtos.stream()
                .map(this::convertToPublicDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(publicDtos, HttpStatus.OK);
    }

    private SalesPersonPublicDTO convertToPublicDTO(SalesPersonDTO salesPersonDto) {
        SalesPersonPublicDTO publicDto = new SalesPersonPublicDTO();
        publicDto.setId(salesPersonDto.getId());
        publicDto.setFirstName(salesPersonDto.getFirstName());
        publicDto.setLastName(salesPersonDto.getLastName());
        publicDto.setEmail(salesPersonDto.getEmail());
        publicDto.setPhone(salesPersonDto.getPhone());
        publicDto.setDepartment(salesPersonDto.getDepartment());
        publicDto.setPosition(salesPersonDto.getPosition());
        publicDto.setHireDate(salesPersonDto.getHireDate());
        publicDto.setTerritory(salesPersonDto.getTerritory());
        publicDto.setSalesQuota(salesPersonDto.getSalesQuota());
        publicDto.setSalesYTD(salesPersonDto.getSalesYTD());
        publicDto.setIsActive(salesPersonDto.getIsActive());
        publicDto.setLastLogin(salesPersonDto.getLastLogin());
        return publicDto;
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
