package com.stock.inventorymanagement.service;

import com.stock.inventorymanagement.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto findByUsername(String username);

    List<UserDto> getAllUsers();
    void registerUser(UserDto userDto);
    void updateUserRoles(Long userId, List<String> roleNames);

}
