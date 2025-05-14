package com.example.demo.dto;

import com.example.demo.entity.Board;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardResponseDto {
    private Long id;
    private LocalDateTime createdAt;
    private String writer;
    private String title;
    private String contents;
    private Long views;

    public BoardResponseDto(Board board){
        this.id = board.getId();
        this.createdAt = board.getCreatedAt();
        this.writer = board.getWriter();
        this.title = board.getTitle();
        this.contents = board.getContents();
        this.views = board.getViews();
    }

}
