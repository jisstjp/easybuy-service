package com.stock.inventorymanagement.service.impl;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.stock.inventorymanagement.domain.Role;
import com.stock.inventorymanagement.domain.User;
import com.stock.inventorymanagement.repository.UserRepository;
import  org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
    private UserRepository userRepository;
	

	@Autowired
	private PasswordEncoder bcryptEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	 User user = userRepository.findByUsername(username)
    			 
                 .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    	 Set<GrantedAuthority> grantedAuthorities = user.getRoles().stream()
                 .map(Role::getName)
                 .map(SimpleGrantedAuthority::new)
                 .collect(Collectors.toSet());

         return new org.springframework.security.core.userdetails.User(user.getUsername(), bcryptEncoder.encode(user.getPassword()),
                 grantedAuthorities);
         

       
    }
    
    

}
