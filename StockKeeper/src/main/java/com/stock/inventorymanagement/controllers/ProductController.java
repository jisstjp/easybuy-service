package com.stock.inventorymanagement.controllers;

import com.stock.inventorymanagement.dto.ProductDto;
import com.stock.inventorymanagement.dto.ProductSearchCriteria;
import com.stock.inventorymanagement.exception.ErrorResponse;
import com.stock.inventorymanagement.exception.InvalidPriceTypeException;
import com.stock.inventorymanagement.exception.ResourceNotFoundException;
import com.stock.inventorymanagement.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController extends BaseController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<Page<ProductDto>> getAllProducts(HttpServletRequest request, Authentication authentication, @RequestParam(value = "page", defaultValue = "0") int page,
                                                           @RequestParam(value = "size", defaultValue = "10") int size,
                                                           @RequestParam(value = "sort", defaultValue = "id,asc") String sort) {

        Pageable pageable = PageRequest.of(page, size, getSort(sort));
        boolean isAdminOrManager = isAdminOrManager(request, authentication);

        Page<ProductDto> products = productService.getAllProducts(pageable, isAdminOrManager);


        // Return the paginated list in the response entity
        return ResponseEntity.ok(products);
    }

    @PostMapping
    public ResponseEntity<?> createProduct(HttpServletRequest request, @RequestBody ProductDto productDto) {
        try {
            Long userId = getUserIdFromToken(request);
            ProductDto createdProduct = productService.createProduct(productDto, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
        } catch (InvalidPriceTypeException ex) {
            ErrorResponse errorResponse = new ErrorResponse("Invalid Price Type", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception ex) {
            ErrorResponse errorResponse = new ErrorResponse("Internal Server Error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PutMapping("/{productId}")
    public ResponseEntity<?> updateProduct(HttpServletRequest request, @PathVariable Long productId,
                                           @RequestBody ProductDto productDto) {
        try {
            Long userId = getUserIdFromToken(request);
            ProductDto updatedProduct = productService.updateProduct(productId, productDto, userId);
            return ResponseEntity.ok(updatedProduct);
        } catch (ResourceNotFoundException e) {
            // Handle the exception and return an error response
            ErrorResponse errorResponse = new ErrorResponse("Resource Not Found", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (InvalidPriceTypeException e) {
            // Handle the exception and return an error response
            ErrorResponse errorResponse = new ErrorResponse("Invalid Price Type", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            // Handle other exceptions and return an error response
            ErrorResponse errorResponse = new ErrorResponse("Internal Server Error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductById(HttpServletRequest request, Authentication authentication, @PathVariable Long productId) {
        try {
            boolean isAdminOrManager = isAdminOrManager(request, authentication);
            ProductDto product = productService.getProductById(productId, isAdminOrManager);
            return ResponseEntity.ok(product);
        } catch (ResourceNotFoundException ex) {
            ErrorResponse errorResponse = new ErrorResponse("Product not found", ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception ex) {
            ErrorResponse errorResponse = new ErrorResponse("Internal Server Error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/search")
    public Page<ProductDto> searchProducts(HttpServletRequest request, @RequestBody ProductSearchCriteria searchCriteria, Authentication authentication) {
        int page = searchCriteria.getPage();
        int size = searchCriteria.getSize();
        Pageable pageable = PageRequest.of(page, size);
        boolean isAdminOrManager = isAdminOrManager(request, authentication);
        return productService.searchProducts(searchCriteria, pageable, isAdminOrManager);
    }

    private Sort getSort(String sort) {
        String[] sortParams = sort.split(",");
        String sortBy = sortParams[0];
        String sortOrder = sortParams[1];

        Sort.Direction direction = sortOrder.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;

        return Sort.by(direction, sortBy);
    }
}
