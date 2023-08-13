package com.stock.inventorymanagement.dto;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value = { "createdBy", "updatedBy", "isDeleted" })
public class ProductDto {

    private Long id;
    private String name;
    private String description;
    private ManufacturerDto manufacturer;
    private BrandDto brand;
    private CategoryDto category;
    private SubcategoryDto subcategory;
    private List<PriceDto> prices;
    private String sku;
    private BigDecimal price;
    private Integer quantity;
    private Integer quantityInBox;
    private BigDecimal weight;
    private BigDecimal length;
    private BigDecimal width;
    private BigDecimal height;
    private String imageUrl;
    private String upc;
    private String barCode;
    private String flavor;
    private Boolean isAvailable = true;

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public BrandDto getBrand() {
	return brand;
    }

    public void setBrand(BrandDto brand) {
	this.brand = brand;
    }

    public CategoryDto getCategory() {
	return category;
    }

    public void setCategory(CategoryDto category) {
	this.category = category;
    }

    public SubcategoryDto getSubcategory() {
	return subcategory;
    }

    public void setSubcategory(SubcategoryDto subcategory) {
	this.subcategory = subcategory;
    }

    public List<PriceDto> getPrices() {
	return prices;
    }

    public void setPrices(List<PriceDto> prices) {
	this.prices = prices;
    }

    public String getSku() {
	return sku;
    }

    public void setSku(String sku) {
	this.sku = sku;
    }

    public BigDecimal getPrice() {
	return price;
    }

    public void setPrice(BigDecimal price) {
	this.price = price;
    }

    public Integer getQuantity() {
	return quantity;
    }

    public void setQuantity(Integer quantity) {
	this.quantity = quantity;
    }

    public Integer getQuantityInBox() {
	return quantityInBox;
    }

    public void setQuantityInBox(Integer quantityInBox) {
	this.quantityInBox = quantityInBox;
    }

    public BigDecimal getWeight() {
	return weight;
    }

    public void setWeight(BigDecimal weight) {
	this.weight = weight;
    }

    public ManufacturerDto getManufacturer() {
	return manufacturer;
    }

    public void setManufacturer(ManufacturerDto manufacturer) {
	this.manufacturer = manufacturer;
    }

    public BigDecimal getLength() {
	return length;
    }

    public void setLength(BigDecimal length) {
	this.length = length;
    }

    public BigDecimal getWidth() {
	return width;
    }

    public void setWidth(BigDecimal width) {
	this.width = width;
    }

    public BigDecimal getHeight() {
	return height;
    }

    public void setHeight(BigDecimal height) {
	this.height = height;
    }

    public String getImageUrl() {
	return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
	this.imageUrl = imageUrl;
    }

    public String getUpc() {
	return upc;
    }

    public void setUpc(String upc) {
	this.upc = upc;
    }

    public Boolean getIsAvailable() {
	return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
	this.isAvailable = isAvailable;
    }

    public String getBarCode() {
	return barCode;
    }

    public void setBarCode(String barCode) {
	this.barCode = barCode;
    }

    public String getFlavor() {
	return flavor;
    }

    public void setFlavor(String flavor) {
	this.flavor = flavor;
    }

}
