package com.springboot.board.data.dto;

import com.springboot.board.data.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class CommentResponseDto {

    private Long id;
    private String comment;
    private String createDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
    private String modifiedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
    private String nickname;
    private Long boardId;

    //Entity -> DTO
    public CommentResponseDto(Comment comment){

        this.id = comment.getId();
        this.comment = comment.getComment();
        this.createDate = comment.getCreatedData();
        this.modifiedDate = comment.getModifiedDate();
        this.nickname = comment.getUser().getNickname();
        this.boardId = comment.getBoard().getNumber();

    }

}
