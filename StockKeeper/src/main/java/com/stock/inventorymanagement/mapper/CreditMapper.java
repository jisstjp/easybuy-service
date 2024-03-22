package com.stock.inventorymanagement.mapper;

import com.stock.inventorymanagement.domain.Credit;
import com.stock.inventorymanagement.dto.CreditDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreditMapper {

    @Autowired
    private ModelMapper modelMapper;

    public CreditDto convertToDTO(Credit credit) {
        return modelMapper.map(credit, CreditDto.class);
    }

    public Credit convertToEntity(CreditDto creditDTO) {
        return modelMapper.map(creditDTO, Credit.class);
    }

    public Credit updateEntityFromDto(CreditDto creditDto, Credit credit) {
        modelMapper.map(creditDto, credit);
        return credit;
    }
}
