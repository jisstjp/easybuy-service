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

import com.stock.inventorymanagement.dto.SubcategoryDto;
import com.stock.inventorymanagement.service.SubcategoryService;

@RestController
@RequestMapping("/api/v1/subcategories")
public class SubcategoryController extends BaseController {

    @Autowired
    private SubcategoryService subcategoryService;

    @GetMapping
    public List<SubcategoryDto> getAllSubcategories() {
	return subcategoryService.getAllSubcategories();
    }

    @GetMapping("/{id}")
    public SubcategoryDto getSubcategoryById(@PathVariable Long id) {
	return subcategoryService.getSubcategoryById(id);
    }

    @PostMapping
    public ResponseEntity<SubcategoryDto> createSubcategory(HttpServletRequest request,
	    @RequestBody SubcategoryDto subcategoryDto) {
	Long userId = getUserIdFromToken(request);
	SubcategoryDto createdSubcategory = subcategoryService.createSubcategory(subcategoryDto, userId);
	return new ResponseEntity<>(createdSubcategory, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public SubcategoryDto updateSubcategory(HttpServletRequest request, @PathVariable Long id,
	    @RequestBody SubcategoryDto subcategoryDto) {
	Long userId = getUserIdFromToken(request);
	return subcategoryService.updateSubcategory(id, subcategoryDto, userId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSubcategory(HttpServletRequest request, @PathVariable Long id) {
	Long userId = getUserIdFromToken(request);
	subcategoryService.deleteSubcategory(id, userId);
	return ResponseEntity.ok().build();
    }

}
