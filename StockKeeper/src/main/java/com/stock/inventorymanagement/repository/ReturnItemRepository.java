package com.stock.inventorymanagement.repository;

import com.stock.inventorymanagement.domain.ReturnItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReturnItemRepository extends JpaRepository<ReturnItem, Long> {
    boolean existsByOrderItemId(Long orderItemId);
}

