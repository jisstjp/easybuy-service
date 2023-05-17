package com.stock.inventorymanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stock.inventorymanagement.domain.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
