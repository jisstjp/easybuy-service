package com.stock.inventorymanagement.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stock.inventorymanagement.dto.UserDto;
import com.stock.inventorymanagement.service.UserService;
import com.stock.inventorymanagement.util.JwtTokenUtil;

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

}
