package com.stock.inventorymanagement.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stock.inventorymanagement.dto.UserDto;
import com.stock.inventorymanagement.service.UserService;
import com.stock.inventorymanagement.util.JwtTokenUtil;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class BaseController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    protected Long getUserIdFromToken(HttpServletRequest request) {
        String authToken = request.getHeader("Authorization");
        String token = authToken.substring(7);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        UserDto userDto = userService.findByUsername(username);
        return userDto.getId();
    }

    protected boolean isAdminOrManager(HttpServletRequest request, Authentication authentication) {
        String authToken = request.getHeader("Authorization");
        if (authToken != null && authToken.startsWith("Bearer ")) {
            String token = authToken.substring(7);

            if (authentication != null) {
                Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
                return authorities.stream()
                        .map(GrantedAuthority::getAuthority)
                        .anyMatch(authority -> authority.equalsIgnoreCase("ROLE_ADMIN") || authority.equalsIgnoreCase("ROLE_MANAGER"));
            }
        }
        return false; // If authentication is not available or user has no matching authority
    }

    protected boolean hasAnyRole(HttpServletRequest request, Authentication authentication, String... roles) {
        String authToken = request.getHeader("Authorization");
        if (authToken != null && authToken.startsWith("Bearer ")) {
            String token = authToken.substring(7);

            if (authentication != null) {
                Set<String> requiredRoles = Arrays.stream(roles)
                        .map(String::toUpperCase)
                        .collect(Collectors.toSet());

                Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
                return authorities.stream()
                        .map(GrantedAuthority::getAuthority)
                        .anyMatch(authority -> requiredRoles.contains(authority.toUpperCase()));
            }
        }
        return false; // If authentication is not available or user has no matching authority
    }


}
