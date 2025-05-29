package com.example.demo.repository;

import com.example.demo.entity.PostFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostFileRepository extends JpaRepository<PostFile, Long> {

    // 게시글의 파일들 조회
    List<PostFile> findByPostIdOrderByFileOrderAsc(Long postId);

    // 파일 타입별 조회
    @Query("SELECT pf FROM PostFile pf WHERE pf.mimeType LIKE :mimeTypePattern")
    List<PostFile> findByMimeTypePattern(@Param("mimeTypePattern") String mimeTypePattern);

    // 특정 게시글의 이미지만 조회
    @Query("SELECT pf FROM PostFile pf WHERE pf.post.id = :postId AND pf.mimeType LIKE 'image/%' ORDER BY pf.fileOrder")
    List<PostFile> findImagesByPostId(@Param("postId") Long postId);
}
