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

import com.stock.inventorymanagement.dto.VendorDto;
import com.stock.inventorymanagement.service.VendorService;

@RestController
@RequestMapping("/api/v1/vendors")
public class VendorController extends BaseController {

    @Autowired
    private VendorService vendorService;

    @PostMapping
    public ResponseEntity<VendorDto> createVendor(HttpServletRequest request, @RequestBody VendorDto vendorDto) {
	Long userId = getUserIdFromToken(request);
	VendorDto createdVendorDto = vendorService.createVendor(vendorDto, userId);
	return new ResponseEntity<>(createdVendorDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendorDto> getVendorById(@PathVariable Long id) {
	VendorDto vendorDto = vendorService.getVendorById(id);
	return new ResponseEntity<>(vendorDto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<VendorDto>> getAllVendors() {
	List<VendorDto> vendorDtos = vendorService.getAllVendors();
	return new ResponseEntity<>(vendorDtos, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VendorDto> updateVendor(HttpServletRequest request, @PathVariable Long id,
	    @RequestBody VendorDto vendorDto) {
	Long userId = getUserIdFromToken(request);
	VendorDto updatedVendorDto = vendorService.updateVendor(id, vendorDto, userId);
	return new ResponseEntity<>(updatedVendorDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVendor(HttpServletRequest request, @PathVariable Long id) {
	Long userId = getUserIdFromToken(request);
	vendorService.deleteVendor(id, userId);
	return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
