package com.stock.inventorymanagement.service;

import java.util.List;
import com.stock.inventorymanagement.dto.BrandDto;

public interface BrandService {

	List<BrandDto> getAllBrands();

	BrandDto getBrandById(Long id);

	BrandDto createBrand(BrandDto brandDto,Long userId);

	BrandDto updateBrand(Long id, BrandDto brandDto,Long userId);

	void deleteBrand(Long id,Long userId);

}
