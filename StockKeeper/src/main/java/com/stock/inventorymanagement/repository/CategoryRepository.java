package com.stock.inventorymanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stock.inventorymanagement.domain.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
