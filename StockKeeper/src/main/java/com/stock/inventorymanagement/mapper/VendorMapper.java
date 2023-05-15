package com.stock.inventorymanagement.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.stock.inventorymanagement.domain.Vendor;
import com.stock.inventorymanagement.dto.VendorDto;

@Component
public class VendorMapper {

	@Autowired
	private ModelMapper modelMapper;

	public Vendor toEntity(VendorDto vendorDto) {
		return modelMapper.map(vendorDto, Vendor.class);
	}

	public VendorDto toDto(Vendor vendor) {
		return modelMapper.map(vendor, VendorDto.class);
	}

}
