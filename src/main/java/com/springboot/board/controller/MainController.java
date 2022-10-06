package com.springboot.board.controller;

import com.springboot.board.data.dto.BoardDto;
import com.springboot.board.data.dto.BoardUpdateRequestDto;
import com.springboot.board.data.dto.UserDto;
import com.springboot.board.data.dto.UserSessionDto;
import com.springboot.board.data.entity.Board;
import com.springboot.board.service.BoardService;
import com.springboot.board.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

@Controller
public class MainController {

    private final HttpSession session;
    private final BoardService boardService;
    private final UserService userService;


    @Autowired
    public MainController(HttpSession session, BoardService boardService, UserService userService) {
        this.session = session;
        this.boardService = boardService;
        this.userService = userService;
    }

    /**
     * 메인 페이지
     * 전체 게시글 출력
     * 페이징
     * @PageAbleDefault(size = 10(디폴트), sort = 정렬 기준 필드(변수), direction = ASC/DESC, Pageable pageable = PageableDefault 값을 갖고 있는 변수 선언)
     */
    @GetMapping("board/main")
    public String getMain(Model model, @PageableDefault(size = 10, sort = "number", direction = Sort.Direction.DESC) Pageable pageable){
        Page<Board> boards = boardService.findBoards(pageable);

        model.addAttribute("boards", boards);

//        int nowPage = boards.getPageable().getPageNumber() + 1;
//        int startPage = Math.max(nowPage - 4, 1);
//        int endPage = Math.min(nowPage + 5, boards.getTotalPages());
//
//        model.addAttribute("nowPage", nowPage);
//        model.addAttribute("startPage", startPage);
//        model.addAttribute("endPage", endPage);

        model.addAttribute("previous", pageable.previousOrFirst().getPageNumber());
        model.addAttribute("next", pageable.next().getPageNumber());
        model.addAttribute("hasNext", boards.hasNext());
        model.addAttribute("hasPrev", boards.hasPrevious());

        UserSessionDto user = (UserSessionDto) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user.getNickname());
        }

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


    /**
     * 로그인
     */
    @GetMapping("/auth/join")
    public String join(){
        return "/user/user-join";
    }

    @PostMapping("/auth/joinProc")
    public String joinProc(UserDto userDto) {
        userService.join(userDto);

        return "redirect:/auth/login";
    }

    @GetMapping("/auth/login")
    public String login(Model model){

        // 세션에 user 라는 attribute 값을 만듦 (로그인 체크 시 활용) 
        UserSessionDto user = (UserSessionDto) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user.getNickname());
        }
        return "/user/user-login";
    }

}








