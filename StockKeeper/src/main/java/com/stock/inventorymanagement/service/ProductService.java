package com.stock.inventorymanagement.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.stock.inventorymanagement.dto.ProductDto;
import com.stock.inventorymanagement.dto.ProductSearchCriteria;

public interface ProductService {

    ProductDto createProduct(ProductDto productDto, Long userId);

    ProductDto getProductById(Long id);

    Page<ProductDto> getAllProducts(Pageable pageable);

    ProductDto updateProduct(Long id, ProductDto productDto, Long userId);

    Page<ProductDto> searchProducts(ProductSearchCriteria searchCriteria, Pageable pageable);

    // void deleteProduct(Long id);

}
