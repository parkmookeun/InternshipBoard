package com.example.demo.controller;

import com.example.demo.dto.BoardIdResponseDto;
import com.example.demo.dto.BoardPostRequestDto;
import com.example.demo.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/boards")
    public ResponseEntity<BoardIdResponseDto> postBoard(
            @RequestBody BoardPostRequestDto dto
    ){
        BoardIdResponseDto responseDto = boardService.post(dto);

        return new ResponseEntity<BoardIdResponseDto>(responseDto, HttpStatus.OK);
    }
}
