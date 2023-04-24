package com.stock.inventorymanagement.service.impl;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stock.inventorymanagement.domain.Brand;
import com.stock.inventorymanagement.repository.BrandRepository;
import com.stock.inventorymanagement.service.BrandService;

@Service
public class BrandServiceImpl implements BrandService {
	
	@Autowired
	private BrandRepository brandRepository;

	@Override
	public List<Brand> getAllBrands() {
		return brandRepository.findAll();
	}

	@Override
	public Brand getBrandById(Long id) {
		return brandRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Brand not found with id: " + id));
	}

	@Override
	public Brand createBrand(Brand brand) {
		return brandRepository.save(brand);
	}

	@Override
	public Brand updateBrand(Long id, Brand brand) {
		Brand existingBrand = getBrandById(id);
		existingBrand.setName(brand.getName());
		existingBrand.setDescription(brand.getDescription());
		existingBrand.setLogoUrl(brand.getLogoUrl());
		return brandRepository.save(existingBrand);
	}

	@Override
	public void deleteBrand(Long id) {
		Brand existingBrand = getBrandById(id);
		brandRepository.delete(existingBrand);
	}

}
