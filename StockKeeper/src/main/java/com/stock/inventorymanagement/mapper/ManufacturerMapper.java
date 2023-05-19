package com.stock.inventorymanagement.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stock.inventorymanagement.domain.Manufacturer;
import com.stock.inventorymanagement.dto.ManufacturerDto;

@Component
public class ManufacturerMapper {

    @Autowired
    private ModelMapper modelMapper;

    public ManufacturerDto toDto(Manufacturer manufacturer) {
	return modelMapper.map(manufacturer, ManufacturerDto.class);
    }

    public Manufacturer toEntity(ManufacturerDto manufacturerDto) {
	return modelMapper.map(manufacturerDto, Manufacturer.class);
    }

}
