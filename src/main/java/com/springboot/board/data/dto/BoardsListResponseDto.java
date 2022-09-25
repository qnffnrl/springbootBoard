package com.springboot.board.data.dto;

import com.springboot.board.data.entity.Board;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardsListResponseDto {

    private Long number;
    private String title;
    private String content;
    private String writer;
    private Long view;
    private LocalDateTime createAt;


    public BoardsListResponseDto(Board board) {
        this.number = board.getNumber();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.writer = board.getWriter();
        this.view = board.getView();
        this.createAt = board.getCreateAt();
    }
}
