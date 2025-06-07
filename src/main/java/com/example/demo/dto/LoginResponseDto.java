package com.example.demo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class LoginResponseDto {
    private String token;
    private String nickName;
    private String type;

    public LoginResponseDto(String token, String nickName) {
        this.token = token;
        this.nickName = nickName;
        this.type = "Bearer";
    }
}
