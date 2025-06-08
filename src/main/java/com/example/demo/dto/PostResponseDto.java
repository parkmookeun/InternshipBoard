package com.example.demo.dto;

import com.example.demo.entity.Comment;
import com.example.demo.entity.Post;
import com.example.demo.entity.PostFile;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Getter
public class PostResponseDto {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String writer;
    private String title;
    private String contents;
    private Long views;
    private List<PostFile> fileList;

    public PostResponseDto(Post post){
        this.id = post.getId();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.writer = post.getWriter();
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.views = post.getViews();
        this.fileList = post.getPostFiles();
    }

}
