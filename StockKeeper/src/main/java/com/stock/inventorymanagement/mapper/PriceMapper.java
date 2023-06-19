package com.stock.inventorymanagement.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stock.inventorymanagement.domain.Price;
import com.stock.inventorymanagement.dto.PriceDto;

@Component
public class PriceMapper {

    @Autowired
    private ModelMapper modelMapper;

    public PriceDto toDto(Price price) {
	return modelMapper.map(price, PriceDto.class);
	
    }

    public Price toEntity(PriceDto priceDto) {
	return modelMapper.map(priceDto, Price.class);
    }

}
