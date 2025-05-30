package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

// PostFiles 엔티티
@Entity
@Getter
@Setter
@Table(name = "post_files")
public class PostFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(name = "original_name", nullable = false)
    private String originalName;

    @Column(name = "stored_name", nullable = false)
    private String storedName;

    @Column(name = "file_path", nullable = false, length = 1000)
    private String filePath; // S3 URL

    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    @Column(name = "mime_type", nullable = false)
    private String mimeType;

    @Column(name = "file_order")
    private Integer fileOrder = 0;

    // 생성자
    public PostFile() {}

    public PostFile(Post post, String originalName, String storedName,
                    String filePath, Long fileSize, String mimeType, Integer fileOrder) {
        this.post = post;
        this.originalName = originalName;
        this.storedName = storedName;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.mimeType = mimeType;
        this.fileOrder = fileOrder;
    }

    // S3 정보 추출 메서드들
    public String getS3Key() {
        // https://bucket.s3.region.amazonaws.com/key -> key 추출
        if (filePath.contains(".amazonaws.com/")) {
            return filePath.substring(filePath.indexOf(".amazonaws.com/") + 15);
        }
        return null;
    }

    public String getFileType() {
        if (mimeType.startsWith("image/")) return "IMAGE";
        if (mimeType.startsWith("video/")) return "VIDEO";
        if (mimeType.startsWith("audio/")) return "AUDIO";
        if (mimeType.contains("pdf") || mimeType.contains("document")) return "DOCUMENT";
        return "OTHER";
    }
}
