package com.stock.inventorymanagement.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.stock.inventorymanagement.domain.Manufacturer;
import com.stock.inventorymanagement.dto.ManufacturerDto;
import com.stock.inventorymanagement.exception.ResourceNotFoundException;
import com.stock.inventorymanagement.mapper.ManufacturerMapper;
import com.stock.inventorymanagement.repository.ManufacturerRepository;
import com.stock.inventorymanagement.service.ManufacturerService;

@Service
public class ManufacturerServiceImpl implements ManufacturerService {

    @Autowired
    private ManufacturerRepository manufacturerRepository;

    @Autowired
    private ManufacturerMapper manufacturerMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ManufacturerDto createManufacturer(ManufacturerDto manufacturerDto, Long userId) {
	Manufacturer manufacturer = manufacturerMapper.toEntity(manufacturerDto);
	manufacturer.setCreatedBy(userId);
	Manufacturer createdManufacturer = manufacturerRepository.save(manufacturer);
	return manufacturerMapper.toDto(createdManufacturer);
    }

    @Override
    @Transactional(readOnly = true)
    public ManufacturerDto getManufacturerById(Long id) {
	Manufacturer manufacturer = manufacturerRepository.findById(id)
		.orElseThrow(() -> new ResourceNotFoundException("Manufacturer", "id", id));
	return manufacturerMapper.toDto(manufacturer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ManufacturerDto> getAllManufacturers() {
	List<Manufacturer> manufacturers = manufacturerRepository.findAll();
	return manufacturers.stream().map(manufacturerMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ManufacturerDto updateManufacturer(Long id, ManufacturerDto manufacturerDto, Long userId) {
	Manufacturer existingManufacturer = manufacturerRepository.findById(id)
		.orElseThrow(() -> new ResourceNotFoundException("Manufacturer", "id", id));

	Manufacturer updatedManufacturer = manufacturerMapper.toEntity(manufacturerDto);
	updatedManufacturer.setId(existingManufacturer.getId());
	updatedManufacturer.setUpdatedBy(userId);
	Manufacturer savedManufacturer = manufacturerRepository.save(updatedManufacturer);
	return manufacturerMapper.toDto(savedManufacturer);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteManufacturer(Long id, Long userId) {
	Manufacturer existingManufacturer = manufacturerRepository.findById(id)
		.orElseThrow(() -> new ResourceNotFoundException("Manufacturer", "id", id));

	existingManufacturer.setUpdatedBy(userId);
	existingManufacturer.setDeleted(true);
	// Perform soft deletion by updating the isDeleted field
	manufacturerRepository.save(existingManufacturer);
    }

}
