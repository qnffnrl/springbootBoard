package com.springboot.board.data.dto;


import lombok.*;

import java.time.LocalDateTime;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class BoardUpdateRequestDto {

    private String title;
    private String content;
}
