package com.stock.inventorymanagement.repository;

import com.stock.inventorymanagement.domain.Return;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ReturnRepository extends JpaRepository<Return, Long>, JpaSpecificationExecutor<Return> {
}