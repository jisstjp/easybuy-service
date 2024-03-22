package com.stock.inventorymanagement.controllers;

import com.stock.inventorymanagement.dto.UserDto;
import com.stock.inventorymanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{username}")
    public UserDto getUserByUsername(@PathVariable String username) {
        return userService.findByUsername(username);
    }

    @GetMapping("/all")
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/register")
    public void registerUser(@RequestBody UserDto userDto) {
        userService.registerUser(userDto);
    }

    @PutMapping("/{userId}/roles")
    public void updateUserRoles(@PathVariable Long userId, @RequestBody List<String> roleNames) {
        userService.updateUserRoles(userId, roleNames);
    }
}
