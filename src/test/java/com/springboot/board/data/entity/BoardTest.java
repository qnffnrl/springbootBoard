package com.springboot.board.data.entity;

import com.springboot.board.data.dto.BoardDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    /**
     * 테스트 클래스는 public 으로 선언
     * 클래스 명은 관례상 테스트클래스명 + Test 로 작성
     * @Test 어노테이션을 선언한 메서드는 JUnit 이 알아서 수행함
     */
    @Test
    public void getWriter() {
        BoardDto boardDto = BoardDto.builder()
                .writer("testWriter")
                .build();

        final String writer = boardDto.getWriter();

        assertEquals("testWriter", writer);
    }

    @Test
    public void getTitle() {
        BoardDto boardDto = BoardDto.builder()
                .title("testTitle")
                .build();

        final String title = boardDto.getTitle();

        assertEquals("testTitle", title);
    }

    @Test
    public void getContent() {
        BoardDto boardDto = BoardDto.builder()
                .content("testContent")
                .build();

        final String content = boardDto.getContent();

        assertEquals("testContent", content);
    }


}