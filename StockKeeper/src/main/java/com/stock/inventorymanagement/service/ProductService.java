package com.stock.inventorymanagement.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.stock.inventorymanagement.dto.ProductDto;
import com.stock.inventorymanagement.dto.ProductSearchCriteria;

import java.math.BigDecimal;

public interface ProductService {

    ProductDto createProduct(ProductDto productDto, Long userId);

    ProductDto getProductById(Long id,boolean isAdminOrManager);

    Page<ProductDto> getAllProducts(Pageable pageable,boolean isAdminOrManager);

    ProductDto updateProduct(Long id, ProductDto productDto, Long userId);

    Page<ProductDto> searchProducts(ProductSearchCriteria searchCriteria, Pageable pageable,boolean isAdminOrManager);

    // void deleteProduct(Long id);

    String getProductNameById(Long productId);

     BigDecimal getSalesPrice(Long productId);

    BigDecimal getSuggestedPrice(Long productId);

}
