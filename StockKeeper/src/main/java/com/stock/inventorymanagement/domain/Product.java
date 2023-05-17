package com.stock.inventorymanagement.domain;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subcategory_id")
    private Subcategory subcategory;

    @OneToMany(mappedBy = "product")
    private List<Price> prices;

    @Column(nullable = false)
    private String sku;

    private BigDecimal price;

    private Integer quantity;

    @Column(name = "quantity_in_box")
    private Integer quantityInBox;

    private BigDecimal weight;

    private String manufacturer;

    private BigDecimal length;

    private BigDecimal width;

    private BigDecimal height;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "upc")
    private String upc;

    @Column(name = "is_available")
    private Boolean isAvailable = true;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

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

    public Brand getBrand() {
	return brand;
    }

    public void setBrand(Brand brand) {
	this.brand = brand;
    }

    public Category getCategory() {
	return category;
    }

    public void setCategory(Category category) {
	this.category = category;
    }

    public Subcategory getSubcategory() {
	return subcategory;
    }

    public void setSubcategory(Subcategory subcategory) {
	this.subcategory = subcategory;
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

    public BigDecimal getWeight() {
	return weight;
    }

    public void setWeight(BigDecimal weight) {
	this.weight = weight;
    }

    public String getManufacturer() {
	return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
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

    public Long getCreatedBy() {
	return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
	this.createdBy = createdBy;
    }

    public Long getUpdatedBy() {
	return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
	this.updatedBy = updatedBy;
    }

    public Boolean getIsDeleted() {
	return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
	this.isDeleted = isDeleted;
    }

    public List<Price> getPrices() {
	return prices;
    }

    public void setPrices(List<Price> prices) {
	this.prices = prices;
    }

    public Integer getQuantityInBox() {
	return quantityInBox;
    }

    public void setQuantityInBox(Integer quantityInBox) {
	this.quantityInBox = quantityInBox;
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

}
