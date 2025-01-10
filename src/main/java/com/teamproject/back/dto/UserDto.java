package com.teamproject.back.dto;


import com.teamproject.back.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String email;
    private String password;
    private String username;
    //010-1234-5678
    private String phoneNumber;
    private Role role;

    private LocalDate birthday;

}
