package com.stock.inventorymanagement.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stock.inventorymanagement.domain.User;
import com.stock.inventorymanagement.dto.UserDto;

@Component
public class UserMapper {

    @Autowired
    private ModelMapper modelMapper;

    public User toEntity(UserDto userDto) {
	return modelMapper.map(userDto, User.class);
    }

    public UserDto toDto(User user) {
	return modelMapper.map(user, UserDto.class);
    }

}
