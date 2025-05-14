package com.example.demo.controller;

import com.example.demo.dto.BoardIdResponseDto;
import com.example.demo.dto.BoardPostRequestDto;
import com.example.demo.dto.BoardResponseDto;
import com.example.demo.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/boards")
    public ResponseEntity<BoardIdResponseDto> postBoard(
            @RequestBody BoardPostRequestDto dto
    ){
        BoardIdResponseDto responseDto = boardService.postBoard(dto);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/boards/{boardId}")
    public ResponseEntity<BoardResponseDto> findBoard(
            @PathVariable("boardId") Long boardId
    ){
       BoardResponseDto responseDto = boardService.findBoard(boardId);

       return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/boards")
    public ResponseEntity<List<BoardResponseDto>> findBoards(){
       List<BoardResponseDto> responseDto =  boardService.findBoards();

       return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }



}
