package com.stock.inventorymanagement.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stock.inventorymanagement.domain.Product;
import com.stock.inventorymanagement.dto.ProductDto;

@Component
public class ProductMapper {

	@Autowired
	private ModelMapper modelMapper;

	public Product toEntity(ProductDto productDto) {
		return modelMapper.map(productDto, Product.class);
	}

	public ProductDto toDto(Product product) {
		return modelMapper.map(product, ProductDto.class);
	}

}
