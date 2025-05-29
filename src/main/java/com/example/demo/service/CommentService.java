package com.example.demo.service;

import com.example.demo.dto.CommentIdResponseDto;
import com.example.demo.dto.CommentRequestDto;
import com.example.demo.dto.CommentResponseDto;
import com.example.demo.dto.CommentUpdateRequestDto;
import com.example.demo.entity.Comment;
import com.example.demo.entity.Post;
import com.example.demo.global.exception.NotFoundException;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentIdResponseDto createComment(Long postId, CommentRequestDto dto) {
        //해당 포스트가 있는지 먼저 확인 후,
        //있으면 댓글 생성
        Post post = postRepository.findByIdOrElseThrow(postId);

        Comment comment = new Comment(dto.getWriter(),dto.getContents(), post);

        Comment savedComment = commentRepository.save(comment);

        Comment foundComment = commentRepository.findByIdOrElseThrow(savedComment.getId());

        return new CommentIdResponseDto(foundComment.getId());
    }

    public Page<Comment> findComments(Long postId, int pageSize, int pageNumber) {

        postRepository.findByIdOrElseThrow(postId);

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").descending());

        return commentRepository.findByPostId(postId, pageable);
    }

    @Transactional
    public CommentResponseDto updateComment(Long postId, Long commentId, CommentUpdateRequestDto dto) {

        postRepository.findByIdOrElseThrow(postId);

        Comment foundComment = commentRepository.findByIdOrElseThrow(commentId);

        if(!Objects.equals(foundComment.getPost().getId(), postId)){
            throw new NotFoundException(
                    """
                    %d의 게시글에는 %d의 댓글이 없습니다.""".formatted(postId,commentId)
            );
        }

        foundComment.updateCotents(dto.getContents());

        return new CommentResponseDto(foundComment);
    }

    public void deleteComment(Long postId, Long commentId) {

        postRepository.findByIdOrElseThrow(postId);

        Comment foundComment = commentRepository.findByIdOrElseThrow(commentId);

        if(!Objects.equals(foundComment.getPost().getId(), postId)){
            throw new NotFoundException(
                    """
                    %d의 게시글에는 %d의 댓글이 없습니다.""".formatted(postId,commentId)
            );
        }

        commentRepository.delete(foundComment);

    }
}
