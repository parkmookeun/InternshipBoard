package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column
    private String writer; // 작성자

    @Column
    private String title; // 글 제목

    @Column
    private String contents; // 글 내용

    @Column
    private Long views; // 조회 수


    public Board(String writer, String title, String contents){
        this.writer = writer;
        this.title = title;
        this.contents = contents;
        this.views = 0L;
    }
}
