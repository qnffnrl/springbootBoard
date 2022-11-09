package com.springboot.board.data.dto;

import com.springboot.board.data.entity.Board;
import com.springboot.board.data.entity.Comment;
import com.springboot.board.data.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentRequestDto {

    private Long id;
    private String comment;
    private String createDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
    private String modifiedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
    private User user;
    private Board board;

    //DTO -> Entity
    public Comment toEntity(){
        Comment comments = Comment.builder()
                .id(id)
                .comment(comment)
                .createdData(createDate)
                .modifiedDate(modifiedDate)
                .user(user)
                .board(board)
                .build();

        return comments;
    }

}
