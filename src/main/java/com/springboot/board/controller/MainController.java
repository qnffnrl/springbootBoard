package com.springboot.board.controller;

import com.springboot.board.data.dto.BoardDto;
import com.springboot.board.data.dto.BoardResponseDto;
import com.springboot.board.data.entity.Board;
import com.springboot.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class MainController {

    private final BoardService boardService;

    @Autowired
    public MainController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("board/main")
    public String getMain(Model model){
//        List<Board> boards = boardService.findBoards();
        model.addAttribute("boards", boardService.findBoards());

        return "board/main";
    }

    @GetMapping("board/new")
    public String createContent() {
        return "board/createContentForm";
    }

    @PostMapping("/board/new")
    public String create(BoardDto boardDto){

        Board board = new Board();
        board.setTitle(boardDto.getTitle());
        board.setContent(boardDto.getContent());
        board.setCreateAt(LocalDateTime.now());

        //임시
        board.setWriter("risker");
        board.setView(1L);
        //  <-- !

        boardService.boardRegistration(board);

        return "redirect:/board/main";

    }

    @GetMapping("/board/content/{number}")
    public String contentView(@PathVariable Long number, Model model) {

        //흐름 이해할려고 코드 수 줄이지 않음
        ResponseEntity<Board> dto = boardService.findById(number);


        /**
         *  ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
         *  controller(현재 여기) - service - repository - entity(domain) 갖다옴
         *  ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
         */
        System.out.println(dto.toString());
        model.addAttribute("board", dto);

        return "/board/contentView";

    }


}








