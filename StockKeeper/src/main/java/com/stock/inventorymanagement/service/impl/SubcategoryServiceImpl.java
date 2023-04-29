package com.stock.inventorymanagement.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.stock.inventorymanagement.domain.Subcategory;
import com.stock.inventorymanagement.dto.SubcategoryDto;
import com.stock.inventorymanagement.exception.ResourceNotFoundException;
import com.stock.inventorymanagement.mapper.SubcategoryMapper;
import com.stock.inventorymanagement.repository.SubcategoryRepository;
import com.stock.inventorymanagement.service.SubcategoryService;

@Service
public class SubcategoryServiceImpl implements SubcategoryService{
	
	   @Autowired
	    private SubcategoryRepository subcategoryRepository;

	    @Autowired
	    private SubcategoryMapper subcategoryMapper;
	    
	    @Override
		@Transactional(readOnly = true)
	    public List<SubcategoryDto> getAllSubcategories() {
	        List<Subcategory> subcategories = subcategoryRepository.findAll();
	        return subcategories.stream()
	                .map(subcategoryMapper::toDto)
	                .collect(Collectors.toList());
	    }

	    @Override
		@Transactional(readOnly = true)
	    public SubcategoryDto getSubcategoryById(Long id) {
	        Subcategory subcategory = subcategoryRepository.findById(id)
	                .orElseThrow(() -> new ResourceNotFoundException("Subcategory", "id", id));
	        return subcategoryMapper.toDto(subcategory);
	    }

	    @Override
	    @Transactional(propagation = Propagation.REQUIRED)
	    public SubcategoryDto createSubcategory(SubcategoryDto subcategoryDto,Long userId) {
	        Subcategory subcategory = subcategoryMapper.toEntity(subcategoryDto);
	        subcategory.setCreatedBy(userId);
	        subcategory = subcategoryRepository.save(subcategory);
	        return subcategoryMapper.toDto(subcategory);
	    }

	    @Override
	    @Transactional(propagation = Propagation.REQUIRED)
	    public SubcategoryDto updateSubcategory(Long id, SubcategoryDto subcategoryDto,Long userId) {
	        Subcategory existingSubcategory = subcategoryRepository.findById(id)
	                .orElseThrow(() -> new ResourceNotFoundException("Subcategory", "id", id));
	        existingSubcategory.setName(subcategoryDto.getName());
	        existingSubcategory.setDescription(subcategoryDto.getDescription());
	        existingSubcategory.setUpdatedBy(userId);
	        Subcategory updatedSubcategory = subcategoryRepository.save(existingSubcategory);
	        return subcategoryMapper.toDto(updatedSubcategory);
	    }

	    @Override
	    @Transactional(propagation = Propagation.REQUIRED)
	    public void deleteSubcategory(Long id,Long userId) {
	        Subcategory subcategory = subcategoryRepository.findById(id)
	                .orElseThrow(() -> new ResourceNotFoundException("Subcategory", "id", id));
	        subcategory.setDeleted(true);
	        subcategory.setUpdatedBy(userId);
	        subcategoryRepository.save(subcategory);
	    }

}
