package com.stock.inventorymanagement.dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.stock.inventorymanagement.enums.PriceType;

@JsonIgnoreProperties(value = { "createdBy", "updatedBy", "isDeleted" })
public class PriceDto {

    private Long id;

    private Long productId;

    private PriceType priceType;

    private double price;

    private String currency;

    private Long createdBy;

    private Long updatedBy;

    private Boolean isDeleted;

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public Long getProductId() {
	return productId;
    }

    public void setProductId(Long productId) {
	this.productId = productId;
    }

    public PriceType getPriceType() {
	return priceType;
    }

    public void setPriceType(PriceType priceType) {
	this.priceType = priceType;
    }

    public double getPrice() {
	return price;
    }

    public void setPrice(double price) {
	this.price = price;
    }

    public String getCurrency() {
	return currency;
    }

    public void setCurrency(String currency) {
	this.currency = currency;
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

}
