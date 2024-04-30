package com.stock.inventorymanagement.service;

import com.stock.inventorymanagement.domain.SalesPerson;
import com.stock.inventorymanagement.dto.SalesPersonDTO;

import java.util.List;
import java.util.Optional;

public interface SalesPersonService {
    SalesPersonDTO saveSalesPerson(SalesPersonDTO salesPersonDto);
    Optional<SalesPersonDTO> findSalesPersonById(Long id);
    Optional<SalesPersonDTO> findSalesPersonByEmail(String email);
    List<SalesPersonDTO> findAllSalesPersons();
    void deleteSalesPersonById(Long id);
    public SalesPersonDTO updateSalesPerson(Long id, SalesPersonDTO salesPersonDto);
}
