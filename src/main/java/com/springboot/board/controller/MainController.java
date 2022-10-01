package com.springboot.board.controller;

import com.springboot.board.data.dto.BoardDto;
import com.springboot.board.data.dto.BoardUpdateRequestDto;
import com.springboot.board.data.entity.Board;
import com.springboot.board.service.BoardService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
public class MainController {

    private final BoardService boardService;

    @Autowired
    public MainController(BoardService boardService) {
        this.boardService = boardService;
    }

    /**
     * 메인 페이지
     * 전체 게시글 출력
     */
    @GetMapping("board/main")
    public String getMain(Model model){
//        List<Board> boards = boardService.findBoards();
        model.addAttribute("boards", boardService.findBoards());

        return "board/main";
    }

    /**
     * 글 작성 페이지로 이동
     */
    @GetMapping("board/new")
    public String createContent() {
        return "board/createContentForm";
    }

    /**
     *  글 작성 insert (DB 적용)
     */
    @PostMapping("/board/new")
    public String create(BoardDto boardDto){

        Board board = new Board();
        board.setTitle(boardDto.getTitle());
        board.setContent(boardDto.getContent());
        board.setUpdateAt(LocalDateTime.now());

        //임시
        board.setWriter("risker");
        board.setView(1L);
        //  <-- !

        boardService.boardRegistration(board);

        return "redirect:/board/main";

    }

    /**
     *  게시글 상세 페이지로 이동 (제목 클릭 시)
     */
    @GetMapping("/board/content/{number}")
    public String contentView(@PathVariable Long number, Model model) {

        /**
         *  ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
         *  controller(현재 여기) - service - repository - entity(domain) 갖다옴
         *  ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
         */
//        System.out.println("======= DTO : " + dto.toString());
        model.addAttribute("board", boardService.findById(number));

        return "/board/contentView";

    }

    /**
     * 게시글 상세 페이지에서
     * 수정 폼으로 변환
     */
    @GetMapping("/board/updateForm/{number}")
    public String updateForm(@PathVariable("number") Long number, Model model){
        model.addAttribute("board", boardService.findById(number));

        return "/board/updateForm";
    }

    @PostMapping("/board/update/{number}")
    public String updateComplete(@PathVariable Long number, BoardUpdateRequestDto boardUpdateRequestDto){

        Board boardTmp = boardService.findById(number);

        boardTmp.setTitle(boardUpdateRequestDto.getTitle());
        boardTmp.setContent(boardUpdateRequestDto.getContent());
        boardTmp.setUpdateAt(LocalDateTime.now());

        boardService.boardRegistration(boardTmp);

        return "redirect:/board/content/" + number;

    }

    /**
     *  게시글 삭제
     */
    @GetMapping("/board/delete/{number}")
    public String boardDelete(@PathVariable("number") Long number){

        boardService.boardDelete(number);

        return "redirect:/board/main";
    }

}








