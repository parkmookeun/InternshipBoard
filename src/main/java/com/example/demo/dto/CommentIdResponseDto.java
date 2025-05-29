package com.example.demo.dto;

import lombok.Getter;

@Getter
public class CommentIdResponseDto {
    private Long id;

    public CommentIdResponseDto(Long id){
        this.id = id;
    }
}
