package com.example.demo.dto;

import lombok.Getter;

@Getter
public class PostIdResponseDto {
    Long id;

    public PostIdResponseDto(Long id){
        this.id = id;
    }
}
