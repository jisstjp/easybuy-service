package com.stock.inventorymanagement.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.stock.inventorymanagement.domain.Role;
import com.stock.inventorymanagement.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.stock.inventorymanagement.domain.User;
import com.stock.inventorymanagement.dto.UserDto;
import com.stock.inventorymanagement.mapper.UserMapper;
import com.stock.inventorymanagement.repository.UserRepository;
import com.stock.inventorymanagement.service.UserService;

import javax.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDto findByUsername(String username) {
	Optional<User> optionalUser = userRepository.findByUsername(username);
	User user = optionalUser
		.orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
	return userMapper.toDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> {
                    UserDto userDto = userMapper.toDto(user);
                    Set<String> roleNames = user.getRoles().stream()
                            .map(Role::getName)
                            .collect(Collectors.toSet());
                    userDto.setRoles(roleNames);
                    return userDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void registerUser(UserDto userDto) {
        User user = userMapper.toEntity(userDto); // Use the UserMapper to convert to User entity

        // Encode the user's password before saving
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        // Assign default role(s) to the user (e.g., ROLE_USER)
        // Assign roles from UserDto to the user
        Set<Role> roles = userDto.getRoles()
                .stream()
                .map(roleName -> roleRepository.findByName(roleName))
                .collect(Collectors.toSet());
        user.setRoles(roles);

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateUserRoles(Long userId, List<String> roleNames) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Set<Role> roles = roleRepository.findAllByNameIn(roleNames);
            user.setRoles(roles);
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }
    }

}
