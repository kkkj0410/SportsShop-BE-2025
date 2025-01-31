package com.teamproject.back.dto.oauth2;

public enum SocialType {
    GOOGLE("google")
    ,NAVER("naver");

    private String name;

    SocialType(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
