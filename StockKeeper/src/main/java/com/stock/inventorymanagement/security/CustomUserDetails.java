package com.stock.inventorymanagement.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class CustomUserDetails extends User {
    
    private static final long serialVersionUID = 1L;
    private final Long userId;

    private  Long cartId;

    public CustomUserDetails(String username, String password, Long userId,Long cartId,
	    Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.userId = userId;
        this.cartId =cartId;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getCartId() {
        return cartId;
    }
}
