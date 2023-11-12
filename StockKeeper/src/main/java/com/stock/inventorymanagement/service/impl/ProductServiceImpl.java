package com.stock.inventorymanagement.service.impl;

import com.stock.inventorymanagement.dao.ProductDao;
import com.stock.inventorymanagement.domain.*;
import com.stock.inventorymanagement.dto.*;
import com.stock.inventorymanagement.enums.PriceType;
import com.stock.inventorymanagement.exception.InvalidPriceTypeException;
import com.stock.inventorymanagement.exception.ResourceNotFoundException;
import com.stock.inventorymanagement.mapper.PriceMapper;
import com.stock.inventorymanagement.mapper.ProductMapper;
import com.stock.inventorymanagement.repository.*;
import com.stock.inventorymanagement.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private ManufacturerRepository manufacturerRepository;
    @Autowired
    private ProductDao productDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ProductDto createProduct(ProductDto productDto, Long userId) {

        final Product product = productMapper.toEntity(productDto);
        product.setCreatedBy(userId);

        // Set manufacturer
        ManufacturerDto manufacturerDto = productDto.getManufacturer();
        if (manufacturerDto != null && manufacturerDto.getId() != null) {
            Manufacturer manufacturer = manufacturerRepository.findById(manufacturerDto.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Manufacturer", "id", manufacturerDto.getId()));
            product.setManufacturer(manufacturer);
        }

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

        product.setIsDeleted(false);

        Product savedProduct = productRepository.save(product);

        final List<PriceDto> priceDtos = productDto.getPrices();
        if (priceDtos != null && !priceDtos.isEmpty()) {
            List<Price> prices = priceDtos.stream().peek(priceDto -> {
                PriceType priceType = priceDto.getPriceType();
                if (priceType == null || !PriceType.isValid(priceType.getType())) {
                    throw new InvalidPriceTypeException("Invalid price type: " + priceDto.getPriceType());
                }
            }).map(priceDto -> {
                Price price = priceMapper.toEntity(priceDto);
                price.setProduct(savedProduct);
                price.setCreatedBy(userId);
                return price;
            }).collect(Collectors.toList());
            priceRepository.saveAll(prices);
            savedProduct.setPrices(prices);
        }

        // Map and return DTO
        return productMapper.toDto(savedProduct);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ProductDto updateProduct(Long id, ProductDto productDto, Long userId) {
        // Check if the product exists
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        // Update the existing product with the new data
        existingProduct.setName(productDto.getName());
        existingProduct.setDescription(productDto.getDescription());
        existingProduct.setSku(productDto.getSku());
        existingProduct.setUpc(productDto.getUpc());
        existingProduct.setPrice(productDto.getPrice());
        existingProduct.setQuantity(productDto.getQuantity());
        existingProduct.setQuantityInBox(productDto.getQuantityInBox());
        existingProduct.setWeight(productDto.getWeight());
        existingProduct.setLength(productDto.getLength());
        existingProduct.setWidth(productDto.getWidth());
        existingProduct.setHeight(productDto.getHeight());
        existingProduct.setImageUrl(productDto.getImageUrl());
        existingProduct.setIsAvailable(productDto.getIsAvailable());
        existingProduct.setBarCode(productDto.getBarCode());
        existingProduct.setFlavor(productDto.getFlavor());

        // Update other fields as needed

        existingProduct.setUpdatedBy(userId);

        // Set manufacturer
        ManufacturerDto manufacturerDto = productDto.getManufacturer();
        if (manufacturerDto != null && manufacturerDto.getId() != null) {
            Manufacturer manufacturer = manufacturerRepository.findById(manufacturerDto.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Manufacturer", "id", manufacturerDto.getId()));
            existingProduct.setManufacturer(manufacturer);
        }

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
        List<Price> existingPrices = existingProduct.getPrices();

        Product savedProduct = productRepository.save(existingProduct);

        // Update existing prices
        List<PriceDto> updatedPriceDtos = productDto.getPrices();
        if (updatedPriceDtos != null && !updatedPriceDtos.isEmpty()) {
            List<Price> updatedPrices = new ArrayList<>();
            for (PriceDto updatedPriceDto : updatedPriceDtos) {
                Long updatedPriceId = updatedPriceDto.getId();
                if (updatedPriceId != null) {
                    // Find the corresponding existing price by ID
                    Price existingPrice = existingPrices.stream().filter(price -> price.getId().equals(updatedPriceId))
                            .findFirst()
                            .orElseThrow(() -> new ResourceNotFoundException("Price", "id", updatedPriceId));

                    // Update the properties of the existing price
                    existingPrice.setPriceType(updatedPriceDto.getPriceType());
                    existingPrice.setPrice(updatedPriceDto.getPrice());
                    existingPrice.setCurrency(updatedPriceDto.getCurrency());
                    // Update other price properties as needed

                    updatedPrices.add(existingPrice);
                } else {
                    // New price, create a new Price object and add it to the updated prices
                    Price newPrice = priceMapper.toEntity(updatedPriceDto);
                    newPrice.setProduct(savedProduct);
                    newPrice.setCreatedBy(userId);
                    // Set other price properties as needed

                    updatedPrices.add(newPrice);
                }
            }

            // Save the updated prices
            priceRepository.saveAll(updatedPrices);

            // Update the existing prices list with the updated prices
            existingPrices.clear();
            existingPrices.addAll(updatedPrices);
        }

        // Map and return the updated product DTO
        return productMapper.toDto(savedProduct);

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
    public Page<ProductDto> getAllProducts(Pageable pageable) {
        Page<Product> productsPage = productRepository.findAll(pageable);
        List<ProductDto> productDtos = productsPage.map(productMapper::toDto).toList();
        return new PageImpl<>(productDtos, pageable, productsPage.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDto> searchProducts(ProductSearchCriteria searchCriteria, Pageable pageable, boolean isAdminOrManager) {
        Page<Product> productsPage = productDao.searchProducts(searchCriteria, pageable);
        List<ProductDto> productDtos = productsPage.map(productMapper::toDto).toList();
        return new PageImpl<>(filterProducts(productDtos, isAdminOrManager), pageable, productsPage.getTotalElements());

    }

    @Override
    @Transactional(readOnly = true)
    public String getProductNameById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
        return product.getName();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getSalesPrice(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        Double salesPrice = product.getPrices().stream()
                .filter(price -> price.getPriceType() == PriceType.SALES_PRICE && !price.isDeleted())
                .map(Price::getPrice)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Price", "type", "SALES_PRICE for product ID: " + productId));

        return BigDecimal.valueOf(salesPrice);
    }

    public List<ProductDto> filterProducts(List<ProductDto> products, boolean isAdminOrManager) {
// Use Java Stream API to filter products based on user role and retain specified price types
        return products.stream()
                .map(product -> {
                    if (isAdminOrManager) {
                        return product; // Admins see all prices
                    } else {
                        // For non-admins, retain only SALES_PRICE and SUGGESTED_SELLING_PRICE prices
                        List<PriceDto> filteredPrices = product.getPrices().stream()
                                .filter(priceDto -> priceDto.getPriceType() == PriceType.SALES_PRICE
                                        || priceDto.getPriceType() == PriceType.SUGGESTED_SELLING_PRICE)
                                .collect(Collectors.toList());
                        product.setPrices(filteredPrices);
                        return product;
                    }
                })
                .collect(Collectors.toList());
    }

}
