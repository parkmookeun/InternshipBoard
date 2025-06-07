package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserSignUpResponseDto {
    private Long id;

    public UserSignUpResponseDto(Long id){
        this.id =  id;
    }
}
