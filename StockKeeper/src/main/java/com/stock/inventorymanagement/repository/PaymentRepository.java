package com.stock.inventorymanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stock.inventorymanagement.domain.Payment;
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
