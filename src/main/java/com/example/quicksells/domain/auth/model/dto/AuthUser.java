package com.example.quicksells.domain.auth.model.dto;

import com.example.quicksells.common.enums.UserRole;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@Getter
public class AuthUser {

    private final Long id;
    private final String email;
    private final UserRole role;
    private final String name;
    private final Collection<? extends GrantedAuthority> authorities;

    public AuthUser(Long id, String email, UserRole role, String name) {
        this.id = id;
        this.email = email;
        this.role = role;
        this.name = name;
        this.authorities = List.of(new SimpleGrantedAuthority(role.getUserRole()));
    }
}
