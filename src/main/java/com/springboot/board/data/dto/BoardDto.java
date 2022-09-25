package com.springboot.board.data.dto;


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

}
