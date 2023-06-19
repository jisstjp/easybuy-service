package com.stock.inventorymanagement.service;

import java.util.List;

import com.stock.inventorymanagement.dto.ManufacturerDto;

public interface ManufacturerService {

    ManufacturerDto createManufacturer(ManufacturerDto manufacturerDto, Long userId);

    ManufacturerDto getManufacturerById(Long id);

    List<ManufacturerDto> getAllManufacturers();

    ManufacturerDto updateManufacturer(Long id, ManufacturerDto manufacturerDto, Long userId);

    void deleteManufacturer(Long id, Long userId);

}
