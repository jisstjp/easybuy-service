package com.stock.inventorymanagement.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stock.inventorymanagement.domain.Category;
import com.stock.inventorymanagement.dto.CategoryDto;

@Component
public class CategoryMapper {
	@Autowired
	private ModelMapper modelMapper;

	public CategoryDto toDto(Category category) {
		return modelMapper.map(category, CategoryDto.class);
	}

	public Category toEntity(CategoryDto categoryDto) {
		return modelMapper.map(categoryDto, Category.class);
	}

}
