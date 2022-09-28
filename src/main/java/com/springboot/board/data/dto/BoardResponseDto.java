package com.springboot.board.data.dto;

import com.springboot.board.data.entity.Board;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class BoardResponseDto {

    private Long number;
    private String title;
    private String content;
    private String writer;
    private Long view;
    private LocalDateTime createAt;

}
