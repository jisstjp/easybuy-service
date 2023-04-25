package com.stock.inventorymanagement.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.stock.inventorymanagement.domain.Brand;
import com.stock.inventorymanagement.dto.BrandDto;
import com.stock.inventorymanagement.mapper.BrandMapper;
import com.stock.inventorymanagement.repository.BrandRepository;
import com.stock.inventorymanagement.service.BrandService;

@Service
public class BrandServiceImpl implements BrandService {
	
	@Autowired
	private BrandRepository brandRepository;
	
    @Autowired
    private BrandMapper brandMapper;

	@Override
	@Transactional(readOnly = true)
	public List<BrandDto> getAllBrands() {
		   List<Brand> brands = brandRepository.findAll();
	    return brands.stream().map(brandMapper::toDto).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public BrandDto getBrandById(Long id) {
		 Brand brand = brandRepository.findById(id)
		            .orElseThrow(() -> new EntityNotFoundException("Brand not found with id: " + id));
		    return brandMapper.toDto(brand);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public BrandDto createBrand(BrandDto brandDto,Long userId) {
		 Brand brand = brandMapper.toEntity(brandDto);
		 brand.setCreatedBy(userId);
	       brand.setUpdatedBy(userId);
		 Brand createdBrand = brandRepository.save(brand);
		 return brandMapper.toDto(createdBrand);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public BrandDto updateBrand(Long id, BrandDto brandDto,Long userId) {
		   Brand existingBrand = brandRepository.findById(id)
	                .orElseThrow(() -> new EntityNotFoundException("Brand not found with id: " + id));
	        existingBrand.setName(brandDto.getName());
	        existingBrand.setDescription(brandDto.getDescription());
	        existingBrand.setLogoUrl(brandDto.getLogoUrl());
	        existingBrand.setUpdatedBy(userId);
	        Brand updatedBrand = brandRepository.save(existingBrand);
	        return brandMapper.toDto(updatedBrand);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteBrand(Long id,Long userId) {
		 Brand existingBrand = brandRepository.findById(id)
		            .orElseThrow(() -> new EntityNotFoundException("Brand not found with id: " + id));
		 existingBrand.setUpdatedBy(userId);
		 existingBrand.setDeleted(true);
		    brandRepository.save(existingBrand);
	}

}
