package com.springboot.board.service;

import com.springboot.board.data.dto.BoardsListResponseDto;
import com.springboot.board.data.entity.Board;
import com.springboot.board.repository.BoardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }


    /**
     * 게시글 등록
     */
    public void boardRegistration(Board board){
        boardRepository.save(board);

    }

    /**
     * 전체 게시글 조회
     */
    @Transactional(readOnly = true)
    public List<BoardsListResponseDto> findBoards() {
        return boardRepository.findAll().
                stream().map(BoardsListResponseDto::new).
                collect(Collectors.toList());
    }

    /**
     * 특정 게시글 조회 (1개)
     */
    @Transactional(readOnly = true)
    public Optional<Board> findById(Long number) {

        return boardRepository.findById(number);


    }
}
