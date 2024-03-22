package com.stock.inventorymanagement.mapper;

import com.stock.inventorymanagement.domain.CustomerLicense;
import com.stock.inventorymanagement.dto.CustomerLicenseDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomerLicenseMapper {

    @Autowired
    private ModelMapper modelMapper;

    public CustomerLicenseDto toDto(CustomerLicense customerLicense) {
        return modelMapper.map(customerLicense, CustomerLicenseDto.class);
    }

    public CustomerLicense toEntity(CustomerLicenseDto customerLicenseDTO) {
        return modelMapper.map(customerLicenseDTO, CustomerLicense.class);
    }



}
