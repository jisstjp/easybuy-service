package com.stock.inventorymanagement.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.stock.inventorymanagement.dto.BrandDto;
import com.stock.inventorymanagement.service.BrandService;

@RestController
@RequestMapping("/api/v1/brands")
public class BrandController extends BaseController {

    @Autowired
    private BrandService brandService;

    @GetMapping
    public List<BrandDto> getAllBrands() {
	return brandService.getAllBrands();
    }

    @GetMapping("/{id}")
    public BrandDto getBrandById(@PathVariable Long id) {
	return brandService.getBrandById(id);
    }

    @PostMapping
    public BrandDto createBrand(HttpServletRequest request, @RequestBody BrandDto brandDto) {
	Long userId = getUserIdFromToken(request);
	return brandService.createBrand(brandDto, userId);
    }

    @PutMapping("/{id}")
    public BrandDto updateBrand(HttpServletRequest request, @PathVariable Long id, @RequestBody BrandDto brandDto) {
	Long userId = getUserIdFromToken(request);
	return brandService.updateBrand(id, brandDto, userId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBrand(HttpServletRequest request, @PathVariable Long id) {
	Long userId = getUserIdFromToken(request);
	brandService.deleteBrand(id, userId);
	return ResponseEntity.ok().build();
    }

}
