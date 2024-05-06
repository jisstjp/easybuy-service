package com.stock.inventorymanagement.repository;

import com.stock.inventorymanagement.domain.SalesPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SalesPersonRepository extends JpaRepository<SalesPerson, Long> {
    Optional<SalesPerson> findByEmail(String email);
    Optional<SalesPerson> findByUserId(Long userId);

}
