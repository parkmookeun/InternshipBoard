package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.entity.Comment;
import com.example.demo.entity.Post;
import com.example.demo.service.CommentService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class CommentController {

    private final CommentService commentService;

    //댓글 작성
    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentIdResponseDto> createComment(
            @PathVariable("postId") Long postId,
            @RequestBody CommentRequestDto dto
    ){
        //해당 게시글에 댓글 작성
        CommentIdResponseDto responseDto = commentService.createComment(postId, dto);

        return new ResponseEntity<CommentIdResponseDto>(responseDto, HttpStatus.CREATED);
    }

    //댓글 목록 조회(페이지네이션)
    @GetMapping("/{postId}/comments")
    public ResponseEntity<PageResponseDto<Comment>> findComments(
            @PathVariable("postId") Long postId,
            @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize
    ){
        Page<Comment> commentPage = commentService.findComments(postId, pageSize, pageNumber);

        PageResponseDto<Comment> commentPageResponseDto = PageResponseDto.of(commentPage);

        return new ResponseEntity<>(commentPageResponseDto, HttpStatus.OK);
    }

    //댓글 수정
    @PutMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(
            @PathVariable("postId") Long postId,
            @PathVariable("commentId") Long commentId,
            @RequestBody CommentUpdateRequestDto dto
    ){
       CommentResponseDto responseDto = commentService.updateComment(postId, commentId, dto);

       return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    //댓글 삭제
    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable("postId") Long postId,
            @PathVariable("commentId") Long commentId
    ){
        commentService.deleteComment(postId, commentId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
