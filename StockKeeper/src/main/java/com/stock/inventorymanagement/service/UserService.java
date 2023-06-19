package com.stock.inventorymanagement.service;

import com.stock.inventorymanagement.dto.UserDto;

public interface UserService {

    UserDto findByUsername(String username);

}
