package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class BoardPostRequestDto {

    @NotBlank(message = "작성자는 빈칸일 수 없습니다!")
    private String writer;
    @NotBlank(message = "제목은 빈칸일 수 없습니다!")
    private String title;
    @NotBlank(message = "내용은 빈칸일 수 없습니다!")
    private String contents;
}
