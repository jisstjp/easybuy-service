package com.stock.inventorymanagement.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class CustomUserDetails extends User {
    
    private static final long serialVersionUID = 1L;
    private final Long userId;

    public CustomUserDetails(String username, String password, Long userId,
	    Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

}
