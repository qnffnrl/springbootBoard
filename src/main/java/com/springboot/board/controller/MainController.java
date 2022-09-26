package com.springboot.board.controller;

import com.springboot.board.data.dto.BoardDto;
import com.springboot.board.data.dto.BoardsListResponseDto;
import com.springboot.board.data.entity.Board;
import com.springboot.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.util.List;
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

        boardService.boardRegistration(board);

        return "redirect:/board/main";

    }

    @GetMapping("/board/content/{number}")
    public String contentView(@PathVariable Long number, Model model) {

        Optional<Board> dto = boardService.findById(number);
        model.addAttribute("board", dto);

        return "/board/contentView";

    }


}








