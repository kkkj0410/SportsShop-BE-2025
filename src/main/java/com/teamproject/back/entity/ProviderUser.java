package com.teamproject.back.entity;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface ProviderUser {

    Collection<? extends GrantedAuthority> getAuthorities();
    String getPassword();
    String getUsername();

}
