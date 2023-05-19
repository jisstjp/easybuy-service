package com.stock.inventorymanagement.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stock.inventorymanagement.dto.ManufacturerDto;
import com.stock.inventorymanagement.service.ManufacturerService;

@RestController
@RequestMapping("/api/v1/manufacturers")
public class ManufacturerController extends BaseController {

    @Autowired
    private ManufacturerService manufacturerService;

    @GetMapping
    public ResponseEntity<List<ManufacturerDto>> getAllManufacturers() {
	List<ManufacturerDto> manufacturers = manufacturerService.getAllManufacturers();
	return ResponseEntity.ok(manufacturers);
    }

    @PostMapping
    public ResponseEntity<ManufacturerDto> createManufacturer(HttpServletRequest request,
	    @RequestBody ManufacturerDto manufacturerDto) {
	Long userId = getUserIdFromToken(request);
	ManufacturerDto createdManufacturer = manufacturerService.createManufacturer(manufacturerDto, userId);
	return ResponseEntity.status(HttpStatus.CREATED).body(createdManufacturer);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ManufacturerDto> getManufacturerById(@PathVariable Long id) {
	ManufacturerDto manufacturer = manufacturerService.getManufacturerById(id);
	return ResponseEntity.ok(manufacturer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ManufacturerDto> updateManufacturer(HttpServletRequest request, @PathVariable Long id,
	    @RequestBody ManufacturerDto manufacturerDto) {
	Long userId = getUserIdFromToken(request);
	ManufacturerDto updatedManufacturer = manufacturerService.updateManufacturer(id, manufacturerDto, userId);
	return ResponseEntity.ok(updatedManufacturer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteManufacturer(HttpServletRequest request, @PathVariable Long id) {
	Long userId = getUserIdFromToken(request);
	manufacturerService.deleteManufacturer(id, userId);
	return ResponseEntity.noContent().build();
    }

}
