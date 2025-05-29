package com.example.demo.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PostUpdateRequestDto {
    @NotBlank(message = "작성자는 빈칸일 수 없습니다!")
    private String title;
    @NotBlank(message = "작성자는 빈칸일 수 없습니다!")
    private String contents;

}
