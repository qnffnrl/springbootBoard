package com.springboot.board.repository;

import com.springboot.board.data.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Board save(Board board);

    @Query("SELECT b FROM Board as b ORDER BY b.number DESC")
    List<Board> findAll();

//    @Query("SELECT b FROM Board as b WHERE b.number = Board.number")
    Optional<Board> findById(Long number);

}
