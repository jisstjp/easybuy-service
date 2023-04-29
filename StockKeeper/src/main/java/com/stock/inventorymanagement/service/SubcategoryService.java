package com.stock.inventorymanagement.service;

import java.util.List;

import com.stock.inventorymanagement.dto.SubcategoryDto;

public interface SubcategoryService {

	List<SubcategoryDto> getAllSubcategories();

	SubcategoryDto getSubcategoryById(Long id);

	SubcategoryDto createSubcategory(SubcategoryDto subcategoryDto,Long userId);

	SubcategoryDto updateSubcategory(Long id, SubcategoryDto subcategoryDto,Long userId);

	void deleteSubcategory(Long id,Long userId);

}
