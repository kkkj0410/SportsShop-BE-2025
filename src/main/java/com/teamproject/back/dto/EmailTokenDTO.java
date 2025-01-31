package com.teamproject.back.dto;


import lombok.Data;

@Data
public class EmailTokenDTO {

    private String email;
    private String authCode;
}
