package com.example.demo.dto;


import com.example.demo.entity.Comment;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String writer;
    private String contents;

    public CommentResponseDto(Comment comment){
        this.id = comment.getId();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
        this.writer = comment.getWriter();
        this.contents = comment.getContents();
    }
}
