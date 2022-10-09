package com.springboot.board.repository;

import com.springboot.board.data.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Modifying
    @Query("update Board b set b.view = b.view + 1 where b.number = :number")
    Integer updateView(Long number);


}
