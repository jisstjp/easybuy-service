package com.stock.inventorymanagement.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.stock.inventorymanagement.domain.Vendor;
import com.stock.inventorymanagement.dto.VendorDto;
import com.stock.inventorymanagement.exception.ResourceNotFoundException;
import com.stock.inventorymanagement.mapper.VendorMapper;
import com.stock.inventorymanagement.repository.VendorRepository;
import com.stock.inventorymanagement.service.VendorService;

@Service
public class VendorServiceImpl implements VendorService {

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private VendorMapper vendorMapper;

    @Override
    @Transactional(readOnly = true)
    public VendorDto createVendor(VendorDto vendorDto, Long userId) {
	Vendor vendor = vendorMapper.toEntity(vendorDto);
	vendor.setCreatedBy(userId);
	vendor = vendorRepository.save(vendor);
	return vendorMapper.toDto(vendor);
    }

    @Override
    @Transactional(readOnly = true)
    public VendorDto getVendorById(Long id) {
	Vendor vendor = vendorRepository.findById(id)
		.orElseThrow(() -> new ResourceNotFoundException("Vendor", "id", id));
	return vendorMapper.toDto(vendor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VendorDto> getAllVendors() {
	List<Vendor> vendors = vendorRepository.findAll();
	return vendors.stream().map(vendorMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public VendorDto updateVendor(Long id, VendorDto vendorDto, Long userId) {
	Vendor vendor = vendorRepository.findById(id)
		.orElseThrow(() -> new ResourceNotFoundException("Vendor", "id", id));
	vendor.setName(vendorDto.getName());
	vendor.setAddress(vendorDto.getAddress());
	vendor.setPhoneNumber(vendorDto.getPhoneNumber());
	vendor.setEmail(vendorDto.getEmail());
	vendor.setUpdatedBy(userId);
	vendor = vendorRepository.save(vendor);
	return vendorMapper.toDto(vendor);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteVendor(Long id, Long userId) {
	Vendor vendor = vendorRepository.findById(id)
		.orElseThrow(() -> new ResourceNotFoundException("Vendor", "id", id));
	vendor.setIsDeleted(true);
	vendor.setUpdatedBy(userId);
	vendorRepository.save(vendor);
    }
}
