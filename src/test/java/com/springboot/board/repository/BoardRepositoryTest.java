package com.springboot.board.repository;

import com.springboot.board.data.dto.BoardDto;
import com.springboot.board.data.entity.Board;
import com.springboot.board.service.BoardService;
import org.apache.tomcat.jni.Local;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;


class BoardRepositoryTest {

    /**
     * CRUD 테스트
     */
    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void create(){
        Board board = new Board();

        board.setNumber(999L);
        board.setTitle("testTitle");
        board.setWriter("testWriter");
        board.setContent("testContent");
        board.setUpdateAt(LocalDate.now());

        //debug : boardRepository NullPointException 발생
//        Board newBoard = boardRepository.save(board);
//        System.out.println("newBoard : " + newBoard);

    }
}