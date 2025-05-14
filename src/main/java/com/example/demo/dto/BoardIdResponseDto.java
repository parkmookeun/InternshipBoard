package com.example.demo.dto;

import lombok.Getter;

@Getter
public class BoardIdResponseDto {
    Long id;

    public BoardIdResponseDto(Long id){
        this.id = id;
    }
}
