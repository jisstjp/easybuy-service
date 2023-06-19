package com.stock.inventorymanagement.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stock.inventorymanagement.domain.Subcategory;
import com.stock.inventorymanagement.dto.SubcategoryDto;

@Component
public class SubcategoryMapper {

    @Autowired
    private ModelMapper modelMapper;

    public SubcategoryDto toDto(Subcategory subcategory) {
	return modelMapper.map(subcategory, SubcategoryDto.class);
    }

    public Subcategory toEntity(SubcategoryDto subcategoryDto) {
	return modelMapper.map(subcategoryDto, Subcategory.class);
    }

}
