package com.stock.inventorymanagement.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stock.inventorymanagement.dto.ProductDto;
import com.stock.inventorymanagement.service.ProductService;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController extends BaseController{
	
	 @Autowired
     private  ProductService productService;
	 
	 @GetMapping
	    public ResponseEntity<List<ProductDto>> getAllProducts() {
	        List<ProductDto> products = productService.getAllProducts();
	        return ResponseEntity.ok(products);
	    }

	    @PostMapping
	    public ResponseEntity<ProductDto> createProduct(HttpServletRequest request,@RequestBody ProductDto productDto) {
	     	 Long userId = getUserIdFromToken(request);
	        ProductDto createdProduct = productService.createProduct(productDto,userId);
	        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
	    }

}
