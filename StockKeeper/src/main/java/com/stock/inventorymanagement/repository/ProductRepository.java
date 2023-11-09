package com.stock.inventorymanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.stock.inventorymanagement.domain.Product;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {

    @Override
    @Query(value = "SELECT p FROM Product p",
    countQuery = "SELECT COUNT(p) FROM Product p")
    Page<Product> findAll(Pageable pageable);
    
    
    //@Query("SELECT p FROM Product p WHERE p.id = :id AND p.isDeleted = false")
    @Query("SELECT p FROM Product p WHERE p.id = :id AND (p.isDeleted = false OR p.isDeleted IS NULL)")
     Optional<Product> findByIdAndIsDeletedFalse(Long id);
    
    List<Product> findByIdIn(List<Long> productIds);

}
