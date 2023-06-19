package com.stock.inventorymanagement.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.stock.inventorymanagement.domain.Category;
import com.stock.inventorymanagement.dto.CategoryDto;
import com.stock.inventorymanagement.exception.ResourceNotFoundException;
import com.stock.inventorymanagement.mapper.CategoryMapper;
import com.stock.inventorymanagement.repository.CategoryRepository;
import com.stock.inventorymanagement.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> getAllCategories() {
	List<Category> categories = categoryRepository.findAll();
	return categories.stream().map(categoryMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getCategoryById(Long id) {
	Category category = categoryRepository.findById(id)
		.orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
	return categoryMapper.toDto(category);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public CategoryDto createCategory(CategoryDto categoryDto, Long userId) {
	Category category = categoryMapper.toEntity(categoryDto);
	category.setCreatedBy(userId);
	category = categoryRepository.save(category);
	return categoryMapper.toDto(category);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto, Long userId) {
	Category existingCategory = categoryRepository.findById(id)
		.orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
	existingCategory.setName(categoryDto.getName());
	existingCategory.setDescription(categoryDto.getDescription());
	existingCategory.setUpdatedBy(userId);

	Category updatedCategory = categoryRepository.save(existingCategory);
	return categoryMapper.toDto(updatedCategory);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteCategory(Long id, Long userId) {
	Category category = categoryRepository.findById(id)
		.orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
	category.setDeleted(true);
	category.setUpdatedBy(userId);
	categoryRepository.save(category);
    }

}
