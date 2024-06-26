package com.stock.inventorymanagement.dto;

public class BrandDto {
    private Long id;
    private String name;
    private String description;
    private String logoUrl;
    private Long manufacturerId;

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

    public String getLogoUrl() {
	return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
	this.logoUrl = logoUrl;
    }

    public Long getManufacturerId() {
	return manufacturerId;
    }

    public void setManufacturerId(Long manufacturerId) {
	this.manufacturerId = manufacturerId;
    }

}
