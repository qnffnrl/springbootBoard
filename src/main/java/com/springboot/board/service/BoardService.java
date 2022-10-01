package com.springboot.board.service;

import com.springboot.board.data.dto.BoardUpdateRequestDto;
import com.springboot.board.data.dto.BoardsListResponseDto;
import com.springboot.board.data.entity.Board;
import com.springboot.board.repository.BoardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
//    @Transactional(readOnly = true)
    public Board findById(Long number) {

        return boardRepository.findById(number).get();

    }

    /**
     * 게시글 수정
     */
    @Transactional
    public Long update(Long number, BoardUpdateRequestDto requestDto){

        Board board = boardRepository.findById(number).orElseThrow(() ->
                new IllegalArgumentException("No Exist!!"));

        /**
         * JPA의 영속성 컨텍스트 덕분에 entity 객체의 값만 변경하면 자동으로 DB에 반영된다
         * -> repository.update를 쓰지 않아도 됨
         */

        board.update(requestDto.getTitle(), requestDto.getContent());

        return number;
    }


}
