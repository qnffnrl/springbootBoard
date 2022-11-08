package com.springboot.board.data.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "board")
public class Board extends TimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long number;

    @Column(nullable = false)
    private String writer;
    private String title;
    private String content;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private int view;

    /**
     * 외래키 매핑
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    private LocalDate updateAt;
}
