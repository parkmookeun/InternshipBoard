package com.example.demo.dto;

import com.example.demo.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponseDto {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String writer;
    private String title;
    private String contents;
    private Long views;

    public PostResponseDto(Post post){
        this.id = post.getId();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.writer = post.getWriter();
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.views = post.getViews();
    }

}
