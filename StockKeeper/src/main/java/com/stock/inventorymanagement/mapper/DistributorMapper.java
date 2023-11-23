package com.stock.inventorymanagement.mapper;

import com.stock.inventorymanagement.domain.Distributor;
import com.stock.inventorymanagement.dto.DistributorDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DistributorMapper {

    @Autowired
    private ModelMapper modelMapper;

    public DistributorDto toDto(Distributor distributor) {
        return modelMapper.map(distributor, DistributorDto.class);
    }

    public Distributor toEntity(DistributorDto dto) {
        return modelMapper.map(dto, Distributor.class);
    }

    public void updateEntityFromDto(DistributorDto distributorDto, Distributor distributor) {
        if (distributorDto.getName() != null) {
            distributor.setName(distributorDto.getName());
        }
        if (distributorDto.getContactNumber() != null) {
            distributor.setContactNumber(distributorDto.getContactNumber());
        }
        if (distributorDto.getEmail() != null) {
            distributor.setEmail(distributorDto.getEmail());
        }
        if (distributorDto.getAddress() != null) {
            distributor.setAddress(distributorDto.getAddress());
        }
        if (distributorDto.getCity() != null) {
            distributor.setCity(distributorDto.getCity());
        }
        if (distributorDto.getCountry() != null) {
            distributor.setCountry(distributorDto.getCountry());
        }
    }

}
