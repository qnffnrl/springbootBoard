package com.springboot.board.data.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "board")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long number;

    @Column(nullable = false)
    private String writer;
    private String title;
    private String content;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private int view;


    private LocalDateTime updateAt;
}
