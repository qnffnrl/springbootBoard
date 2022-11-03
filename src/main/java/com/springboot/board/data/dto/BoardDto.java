package com.springboot.board.data.dto;


import com.springboot.board.data.entity.User;
import lombok.*;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class BoardDto {

    private String writer;

    private String title;

    private String content;

    private User user;

}
