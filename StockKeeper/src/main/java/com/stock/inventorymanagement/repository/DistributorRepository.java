package com.stock.inventorymanagement.repository;

import com.stock.inventorymanagement.domain.Distributor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistributorRepository extends JpaRepository<Distributor, Long> {
}
