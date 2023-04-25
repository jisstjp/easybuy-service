package com.stock.inventorymanagement.service.impl;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.stock.inventorymanagement.domain.User;
import com.stock.inventorymanagement.dto.UserDto;
import com.stock.inventorymanagement.mapper.UserMapper;
import com.stock.inventorymanagement.repository.UserRepository;
import com.stock.inventorymanagement.service.UserService;
@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDto findByUsername(String username) {
    	Optional<User> optionalUser = userRepository.findByUsername(username);
    	User user = optionalUser.orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return userMapper.toDto(user);
    }

}
