package com.stock.inventorymanagement.mapper;

import com.stock.inventorymanagement.domain.Return;
import com.stock.inventorymanagement.dto.ReturnDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReturnMapper {
    private final ModelMapper modelMapper;

    @Autowired
    public ReturnMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ReturnDTO toDTO(Return returnEntity) {
        return modelMapper.map(returnEntity, ReturnDTO.class);
    }

    public Return toEntity(ReturnDTO returnDTO) {
        return modelMapper.map(returnDTO, Return.class);
    }

    public void updateEntityFromDTO(ReturnDTO returnDTO, Return returnEntity) {
        modelMapper.map(returnDTO, returnEntity);
    }
}
