package com.stock.inventorymanagement.service;

import java.util.List;

import com.stock.inventorymanagement.domain.Brand;

public interface BrandService {

	List<Brand> getAllBrands();

	Brand getBrandById(Long id);

	Brand createBrand(Brand brand);

	Brand updateBrand(Long id, Brand brand);

	void deleteBrand(Long id);

}
