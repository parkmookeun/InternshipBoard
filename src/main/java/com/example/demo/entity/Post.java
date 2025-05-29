package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column
    private LocalDateTime modifiedAt;

    @Column
    private String writer; // 작성자

    @Column
    private String title; // 글 제목

    @Column
    private String contents; // 글 내용

    @Column
    private Long views; // 조회 수

    // 게시글이 삭제되면 댓글도 함께 삭제 (CASCADE)
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // JSON 응답에서 제외 - 무한 순환 참조 방지
    private List<Comment> comments = new ArrayList<>();

    // 파일과의 관계
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("fileOrder ASC")
    private List<PostFile> postFiles = new ArrayList<>();

    public Post(String writer, String title, String contents){
        this.writer = writer;
        this.title = title;
        this.contents = contents;
        this.views = 0L;
    }

    public void increaseViews(){
        this.views++;
    }

    public void updateTitle(String title){
        this.title = title;
    }

    public void updateContents(String contents){
        this.contents = contents;
    }

    // 편의 메서드
    public void addFile(String originalName, String storedName, String filePath,
                        Long fileSize, String mimeType, Integer fileOrder) {
        PostFile postFile = new PostFile();
        postFile.setPost(this);
        postFile.setOriginalName(originalName);
        postFile.setStoredName(storedName);
        postFile.setFilePath(filePath);
        postFile.setFileSize(fileSize);
        postFile.setMimeType(mimeType);
        postFile.setFileOrder(fileOrder != null ? fileOrder : postFiles.size());

        postFiles.add(postFile);
    }

    public List<String> getImageUrls() {
        return postFiles.stream()
                .filter(pf -> pf.getMimeType().startsWith("image/"))
                .map(PostFile::getFilePath)
                .collect(Collectors.toList());
    }
}
