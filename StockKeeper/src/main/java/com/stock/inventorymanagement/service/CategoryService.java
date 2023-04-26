package com.stock.inventorymanagement.service;

import java.util.List;

import com.stock.inventorymanagement.dto.CategoryDto;

public interface CategoryService {

	List<CategoryDto> getAllCategories();

	CategoryDto getCategoryById(Long id);

	CategoryDto createCategory(CategoryDto categoryDto,Long userId);

	CategoryDto updateCategory(Long id, CategoryDto categoryDto,Long userId);

	void deleteCategory(Long id,Long userId);
}
