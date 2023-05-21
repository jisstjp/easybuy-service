package com.stock.inventorymanagement.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.stock.inventorymanagement.domain.Product;
import com.stock.inventorymanagement.dto.ProductSearchCriteria;

public interface ProductDao {

    Page<Product> searchProducts(ProductSearchCriteria searchCriteria, Pageable pageable);

    Long getTotalCount(ProductSearchCriteria searchCriteria);

}
