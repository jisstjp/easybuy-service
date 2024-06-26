package com.stock.inventorymanagement.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stock.inventorymanagement.security.CustomUserDetails;
import com.stock.inventorymanagement.security.JwtRequest;
import com.stock.inventorymanagement.security.JwtResponse;
import com.stock.inventorymanagement.util.JwtTokenUtil;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

	authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

	final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
	final CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
	final String token = jwtTokenUtil.generateToken(customUserDetails);

	return ResponseEntity.ok(new JwtResponse(token));
    }

    private void authenticate(String username, String password) throws Exception {
	try {
	    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
	} catch (DisabledException e) {
	    throw new Exception("USER_DISABLED", e);
	} catch (BadCredentialsException e) {
	    throw new Exception("INVALID_CREDENTIALS", e);
	}
    }

}
