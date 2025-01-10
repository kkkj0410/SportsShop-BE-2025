package com.teamproject.back.entity;

import com.teamproject.back.dto.UserDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserDetailsDto implements ProviderUser{

    private final UserDto userDto;

    public UserDetailsDto(UserDto userDto) {
        this.userDto = userDto;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String authority = "ROLE_" + userDto.getRole();

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(authority));
        return authorities;
    }

    @Override
    public String getPassword() {
        return userDto.getPassword();
    }

    // *******1.10 수정 사항 ***********
    //getEmail()로 보일 수 있게 수정 바람
    @Override
    public String getUsername() {
        return userDto.getUsername();
    }


}
