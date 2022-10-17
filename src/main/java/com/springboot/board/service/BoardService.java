package com.springboot.board.service;

import com.springboot.board.data.entity.Board;
import com.springboot.board.repository.BoardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }


    /**
     * 게시글 등록
     * 게시글 수정할 때도 사용
     */
    public void boardRegistration(Board board){
        boardRepository.save(board);
    }

    /**
     * 전체 게시글 조회
     */
    @Transactional(readOnly = true)
    public Page<Board> findBoards(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    /**
     * 특정 게시글 조회 (1개)
     */
//    @Transactional(readOnly = true)
    public Board findById(Long number) {

        return boardRepository.findById(number).get();

    }

    /**
     * 게시글 삭제
     */
    public void boardDelete(Long number){
        boardRepository.deleteById(number);
    }

    /**
     *  view count
     */
    @Transactional
    public Integer updateView(Long number){
        return boardRepository.updateView(number);
    }

    /**
     * 게시글 검색 기능
     */
    @Transactional
    public Page<Board> search(String keyword, Pageable pageable) {
        Page<Board> searchedList = boardRepository.findByTitleContaining(keyword, pageable);
        return searchedList;
    }

}












