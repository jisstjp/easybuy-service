package com.stock.inventorymanagement.service;

import java.util.List;

import com.stock.inventorymanagement.dto.VendorDto;

public interface VendorService {
	
	List<VendorDto> getAllVendors();

    VendorDto getVendorById(Long id);

    VendorDto createVendor(VendorDto vendorDto,Long userId);

    VendorDto updateVendor(Long id, VendorDto vendorDto,Long userId);

    void deleteVendor(Long id,Long userId);


}
