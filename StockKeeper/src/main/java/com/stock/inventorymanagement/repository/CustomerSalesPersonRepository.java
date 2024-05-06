package com.stock.inventorymanagement.repository;

import com.stock.inventorymanagement.domain.Customer;
import com.stock.inventorymanagement.domain.CustomerSalesPerson;
import com.stock.inventorymanagement.domain.SalesPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerSalesPersonRepository extends JpaRepository<CustomerSalesPerson, Long> {
    List<CustomerSalesPerson> findByCustomerId(Long customerId);
    List<CustomerSalesPerson> findBySalesPersonId(Long salesPersonId);
    Optional<CustomerSalesPerson> findByCustomerAndSalesPerson(Long customerId, Long salesPersonId);

    boolean existsByCustomerAndSalesPerson(Customer customer, SalesPerson salesPerson);

    void deleteAllBySalesPerson(SalesPerson salesPerson);


}
