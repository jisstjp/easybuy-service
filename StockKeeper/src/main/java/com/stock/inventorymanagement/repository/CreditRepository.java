package com.stock.inventorymanagement.repository;

import com.stock.inventorymanagement.domain.Credit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CreditRepository extends JpaRepository<Credit, Long> {
    List<Credit> findByCustomerIdAndStatusAndExpiryDateAfter(Long customerId, String status, Date currentDate);
    List<Credit> findByCustomerIdAndStatus(Long customerId, String status);
}

