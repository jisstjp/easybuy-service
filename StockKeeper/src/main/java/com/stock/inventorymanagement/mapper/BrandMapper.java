package com.stock.inventorymanagement.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stock.inventorymanagement.domain.Brand;
import com.stock.inventorymanagement.dto.BrandDto;

@Component
public class BrandMapper {

    @Autowired
    private ModelMapper modelMapper;

    public BrandDto toDto(Brand brand) {
	return modelMapper.map(brand, BrandDto.class);
    }

    public Brand toEntity(BrandDto brandDto) {
	return modelMapper.map(brandDto, Brand.class);
    }

}
