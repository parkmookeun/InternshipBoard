package com.example.demo.dto;

import lombok.Getter;

@Getter
public class BoardPostRequestDto {
    private String writer;
    private String title;
    private String contents;
}
