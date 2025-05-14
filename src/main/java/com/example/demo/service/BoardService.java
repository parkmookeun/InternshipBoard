package com.example.demo.service;

import com.example.demo.dto.BoardIdResponseDto;
import com.example.demo.dto.BoardPostRequestDto;
import com.example.demo.entity.Board;
import com.example.demo.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public BoardIdResponseDto post(BoardPostRequestDto dto) {
        Board board = new Board(dto.getWriter(), dto.getTitle(), dto.getContents());

        Board savedBoard = boardRepository.save(board);

        Board foundBoard = boardRepository.findByIdOrElseThrow(savedBoard.getId());

        return new BoardIdResponseDto(foundBoard.getId());
    }
}
