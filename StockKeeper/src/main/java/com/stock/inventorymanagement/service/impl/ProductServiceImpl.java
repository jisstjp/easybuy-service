package com.stock.inventorymanagement.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.stock.inventorymanagement.domain.Brand;
import com.stock.inventorymanagement.domain.Category;
import com.stock.inventorymanagement.domain.Price;
import com.stock.inventorymanagement.domain.Product;
import com.stock.inventorymanagement.domain.Subcategory;
import com.stock.inventorymanagement.dto.BrandDto;
import com.stock.inventorymanagement.dto.CategoryDto;
import com.stock.inventorymanagement.dto.PriceDto;
import com.stock.inventorymanagement.dto.ProductDto;
import com.stock.inventorymanagement.dto.SubcategoryDto;
import com.stock.inventorymanagement.enums.PriceType;
import com.stock.inventorymanagement.exception.InvalidPriceTypeException;
import com.stock.inventorymanagement.exception.ResourceNotFoundException;
import com.stock.inventorymanagement.mapper.PriceMapper;
import com.stock.inventorymanagement.mapper.ProductMapper;
import com.stock.inventorymanagement.repository.BrandRepository;
import com.stock.inventorymanagement.repository.CategoryRepository;
import com.stock.inventorymanagement.repository.PriceRepository;
import com.stock.inventorymanagement.repository.ProductRepository;
import com.stock.inventorymanagement.repository.SubcategoryRepository;
import com.stock.inventorymanagement.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private BrandRepository brandRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private SubcategoryRepository subcategoryRepository;
	@Autowired
	private ProductMapper productMapper;
	@Autowired
	private PriceMapper priceMapper;
	@Autowired
	private PriceRepository priceRepository;
	
	@PersistenceContext
    private EntityManager entityManager;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ProductDto createProduct(ProductDto productDto, Long userId) {

		final Product product = productMapper.toEntity(productDto);
		product.setCreatedBy(userId);

		// Set brand
		BrandDto brandDto = productDto.getBrand();
		if (brandDto != null && brandDto.getId() != null) {
			Brand brand = brandRepository.findById(brandDto.getId())
					.orElseThrow(() -> new ResourceNotFoundException("Brand", "id", brandDto.getId()));
			product.setBrand(brand);
		}

		// Set category
		CategoryDto categoryDto = productDto.getCategory();
		if (categoryDto != null && categoryDto.getId() != null) {
			Category category = categoryRepository.findById(categoryDto.getId())
					.orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryDto.getId()));
			product.setCategory(category);
		}

		// Set subcategory
		SubcategoryDto subcategoryDto = productDto.getSubcategory();
		if (subcategoryDto != null && subcategoryDto.getId() != null) {
			Subcategory subcategory = subcategoryRepository.findById(subcategoryDto.getId())
					.orElseThrow(() -> new ResourceNotFoundException("Subcategory", "id", subcategoryDto.getId()));
			product.setSubcategory(subcategory);
		}

		Product savedProduct = productRepository.save(product);

		 // Save prices
	    List<PriceDto> priceDtos = productDto.getPrices();
	    if (priceDtos != null && !priceDtos.isEmpty()) {
	        for (PriceDto priceDto : priceDtos) {
	            PriceType priceType = priceDto.getPriceType();
	            if (priceType == null || !PriceType.isValid(priceType.getType())) {
	                throw new InvalidPriceTypeException("Invalid price type: " + priceDto.getPriceType());
	            }
	            Price price = priceMapper.toEntity(priceDto);
	            price.setProduct(savedProduct);
	            price.setCreatedBy(userId);

	            if (price.getId() != null) {
	                price = entityManager.merge(price);
	            } else {
	                entityManager.persist(price);
	            }
	        }
	    }

		// Map and return DTO
		return productMapper.toDto(product);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ProductDto updateProduct(Long id, ProductDto productDto, Long userId) {
		Product existingProduct = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

		// Update the existing product with the new data
		existingProduct.setName(productDto.getName());
		existingProduct.setDescription(productDto.getDescription());
		// Update other properties as needed

		// Set brand
		BrandDto brandDto = productDto.getBrand();
		if (brandDto != null && brandDto.getId() != null) {
			Brand brand = brandRepository.findById(brandDto.getId())
					.orElseThrow(() -> new ResourceNotFoundException("Brand", "id", brandDto.getId()));
			existingProduct.setBrand(brand);
		}

		// Set category
		CategoryDto categoryDto = productDto.getCategory();
		if (categoryDto != null && categoryDto.getId() != null) {
			Category category = categoryRepository.findById(categoryDto.getId())
					.orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryDto.getId()));
			existingProduct.setCategory(category);
		}

		// Set subcategory
		SubcategoryDto subcategoryDto = productDto.getSubcategory();
		if (subcategoryDto != null && subcategoryDto.getId() != null) {
			Subcategory subcategory = subcategoryRepository.findById(subcategoryDto.getId())
					.orElseThrow(() -> new ResourceNotFoundException("Subcategory", "id", subcategoryDto.getId()));
			existingProduct.setSubcategory(subcategory);
		}

		// Save the updated product
		Product updatedProduct = productRepository.save(existingProduct);

		// Map and return the updated DTO
		return productMapper.toDto(updatedProduct);
	}

	@Override
	@Transactional(readOnly = true)
	public ProductDto getProductById(Long id) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

		return productMapper.toDto(product);
	}

	@Override
	@Transactional(readOnly = true)

	public List<ProductDto> getAllProducts() {
		List<Product> products = productRepository.findAll();
		return products.stream().map(productMapper::toDto).collect(Collectors.toList());
	}

}
