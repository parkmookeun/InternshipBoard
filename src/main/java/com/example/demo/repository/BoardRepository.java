package com.example.demo.repository;

import com.example.demo.entity.Board;
import com.example.demo.global.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long>{

    default Board findByIdOrElseThrow(Long id){
        return findById(id).orElseThrow(() -> new NotFoundException(id + "에 맞는 게시글이 존재하지 않습니다!"));
    }
}
