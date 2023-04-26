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

import com.stock.inventorymanagement.dto.CategoryDto;
import com.stock.inventorymanagement.service.CategoryService;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController extends BaseController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public CategoryDto getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(HttpServletRequest request,@RequestBody CategoryDto categoryDto) {
    	 Long userId = getUserIdFromToken(request);
        CategoryDto createdCategory = categoryService.createCategory(categoryDto,userId);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public CategoryDto updateCategory(HttpServletRequest request,@PathVariable Long id, @RequestBody CategoryDto categoryDto) {
    	Long userId = getUserIdFromToken(request);
        return categoryService.updateCategory(id, categoryDto,userId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(HttpServletRequest request,@PathVariable Long id) {
    	Long userId = getUserIdFromToken(request);
        categoryService.deleteCategory(id,userId);
        return ResponseEntity.ok().build();
    }
}
