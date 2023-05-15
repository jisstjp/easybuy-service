package com.stock.inventorymanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stock.inventorymanagement.domain.Price;

public interface PriceRepository extends JpaRepository<Price, Long> {

}
