package com.stock.inventorymanagement.service;

import java.util.List;

import com.stock.inventorymanagement.dto.ProductDto;

public interface ProductService {

	ProductDto createProduct(ProductDto productDto, Long userId);

    ProductDto getProductById(Long id);

	List<ProductDto> getAllProducts();

	ProductDto updateProduct(Long id, ProductDto productDto, Long userId);

	// void deleteProduct(Long id);

}
