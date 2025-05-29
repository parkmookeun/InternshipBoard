package com.example.demo.repository;

import com.example.demo.entity.Comment;
import com.example.demo.global.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    default Comment findByIdOrElseThrow(Long id){
        return findById(id).orElseThrow(() -> new NotFoundException(id + "에 맞는 댓글이 존재하지 않습니다!"));
    }

    Page<Comment> findByPostId(Long postId, Pageable pageable);
}
