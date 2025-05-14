package com.example.demo.service;

import com.example.demo.dto.BoardIdResponseDto;
import com.example.demo.dto.BoardPostRequestDto;
import com.example.demo.dto.BoardResponseDto;
import com.example.demo.dto.BoardUpdateRequestDto;
import com.example.demo.entity.Board;
import com.example.demo.repository.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    // 게시글 등록
    public BoardIdResponseDto postBoard(BoardPostRequestDto dto) {
        Board board = new Board(dto.getWriter(), dto.getTitle(), dto.getContents());

        Board savedBoard = boardRepository.save(board);

        Board foundBoard = boardRepository.findByIdOrElseThrow(savedBoard.getId());

        return new BoardIdResponseDto(foundBoard.getId());
    }

    //게시글 단건 조회 -> 상세 조회 느낌으로 조회 시마다, 조회 수 + 1
    @Transactional
    public BoardResponseDto findBoard(Long boardId) {

        Board foundBoard = boardRepository.findByIdOrElseThrow(boardId);

        foundBoard.increaseViews();

        return new BoardResponseDto(foundBoard);
    }

    //게시글 다건 조회 -> 모든 게시글 조회 -> 추후에, 페이징 추가
    public List<BoardResponseDto> findBoards() {

        List<Board> boardList = boardRepository.findAll();

        return boardList.stream().map(BoardResponseDto::new).toList();

    }

    //게시글 단건 수정 -> 게시글의 제목과 내용 수정
    @Transactional
    public BoardResponseDto updateBoard(Long boardId, BoardUpdateRequestDto dto) {

        Board foundBoard = boardRepository.findByIdOrElseThrow(boardId);

        foundBoard.updateTitle(dto.getTitle());
        foundBoard.updateContents(dto.getContents());

        return new BoardResponseDto(foundBoard);
    }
}
