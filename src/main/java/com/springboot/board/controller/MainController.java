package com.springboot.board.controller;

import com.springboot.board.annotation.LoginUser;
import com.springboot.board.data.dto.*;
import com.springboot.board.data.entity.Board;
import com.springboot.board.data.entity.Comment;
import com.springboot.board.service.BoardService;
import com.springboot.board.service.UserService;
import com.springboot.board.service.CommentService;
import com.springboot.board.validator.CheckEmailValidator;
import com.springboot.board.validator.CheckNicknameValidator;
import com.springboot.board.validator.CheckUsernameValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
public class MainController {

    private final HttpSession session;
    private final BoardService boardService;
    private final CommentService commentService;
    private final UserService userService;
    private final CheckUsernameValidator checkUsernameValidator;
    private final CheckNicknameValidator checkNicknameValidator;
    private final CheckEmailValidator checkEmailValidator;
    private final AuthenticationManager authenticationManager;

    /**
     * @InitBinder
     * 특정 컨트롤러에서 바인딩 또는 검증 설정을 변경하고 싶을 때 사용
     *
     * @WebDataBinder
     * HTTP 요청 정보를 컨트롤러 메소드의 파라미터나 모델에 바인딩할 때 사용
     */
    @InitBinder
    public void validatorBinder(WebDataBinder binder) {
        binder.addValidators(checkUsernameValidator);
        binder.addValidators(checkNicknameValidator);
        binder.addValidators(checkEmailValidator);
    }

    @Autowired
    public MainController(HttpSession session, BoardService boardService, CommentService commentService, UserService userService, CheckUsernameValidator checkUsernameValidator, CheckNicknameValidator checkNicknameValidator, CheckEmailValidator checkEmailValidator, AuthenticationManager authenticationManager) {
        this.session = session;
        this.boardService = boardService;
        this.commentService = commentService;
        this.userService = userService;
        this.checkUsernameValidator = checkUsernameValidator;
        this.checkNicknameValidator = checkNicknameValidator;
        this.checkEmailValidator = checkEmailValidator;
        this.authenticationManager = authenticationManager;
    }

    /**
     * 메인 페이지
     * 전체 게시글 출력
     * 페이징
     * @PageAbleDefault(size = 10(디폴트), sort = 정렬 기준 필드(변수), direction = ASC/DESC, Pageable pageable = PageableDefault 값을 갖고 있는 변수 선언)
     * @RequestParam : Get Method 에서 키 값의 value 가져올 때 사용
     */
    @GetMapping("board/main")
    public String getMain(@RequestParam(value = "error", required = false) String error,
                          @RequestParam(value = "exception", required = false) String exception,
                          Model model,
                          @PageableDefault(size = 10, sort = "number", direction = Sort.Direction.DESC) Pageable pageable){
        Page<Board> boards = boardService.findBoards(pageable);


//      게시글 뿌려주기
        model.addAttribute("boards", boards);

//        int nowPage = boards.getPageable().getPageNumber() + 1;
//        int startPage = Math.max(nowPage - 4, 1);
//        int endPage = Math.min(nowPage + 5, boards.getTotalPages());
//
//        model.addAttribute("nowPage", nowPage);
//        model.addAttribute("startPage", startPage);
//        model.addAttribute("endPage", endPage);

//      로그인 실패 핸들러 정보
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);



//      페이징 버튼 정보
        model.addAttribute("previous", pageable.previousOrFirst().getPageNumber());
        model.addAttribute("next", pageable.next().getPageNumber());
        model.addAttribute("hasNext", boards.hasNext());
        model.addAttribute("hasPrev", boards.hasPrevious());

        if (error == null) {
            UserSessionDto user = (UserSessionDto) session.getAttribute("user");
            if (user != null) {
                model.addAttribute("user", user.getNickname());
            }
        }
        return "/board/main";
    }

    /**
     * 글 작성 페이지로 이동
     */
    @GetMapping("/board/new")
    public String createContent(Model model) {
        UserSessionDto user = (UserSessionDto) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user.getNickname());
        }
        return "board/createContentForm";
    }

    /**
     *  글 작성 insert (DB 적용)
     */
    @PostMapping("/board/new")
    public String create(BoardDto boardDto, Model model, HttpServletResponse response) throws IOException {

        UserSessionDto user = (UserSessionDto) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user.getNickname());
        }

        Board board = new Board();
        if("".equals(boardDto.getTitle())){
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script>alert('로그인 정보를 확인해주세요.'); history.go(-1);</script>");
            out.flush();
        }

        board.setTitle(boardDto.getTitle());
        board.setContent(boardDto.getContent());
        board.setUpdateAt(LocalDate.now());
        board.setWriter(user.getNickname());

        boardService.boardRegistration(board);

        return "redirect:main";
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

        Board dto = boardService.findById(number);

        /* 댓글 */
        List<Comment> comments = dto.getComments();

        if (comments != null && !comments.isEmpty()) {
            model.addAttribute("comments", comments);
        }

        model.addAttribute("board", dto);
        boardService.updateView(number); //조회수 카운트
        UserSessionDto user = (UserSessionDto) session.getAttribute("user");

        if (user != null) {
            model.addAttribute("user", user.getNickname());
            model.addAttribute("userId", user.getId());

            boolean isBoardWriter = user.getNickname().equals(dto.getWriter());
            model.addAttribute("isBoardWriter", isBoardWriter);


            for(int i = 0; i < comments.size(); i++){
                boolean isReplyWriter = comments.get(i).getUser().getId().equals(user.getId());
                model.addAttribute("isReplyWriter", isReplyWriter);
            }
        }

        return "board/contentView";

    }

    /**
     * 게시글 상세 페이지에서
     * 수정 폼으로 변환
     */
    @GetMapping("/board/updateForm/{number}")
    public String updateForm(@PathVariable("number") Long number, Model model){
        model.addAttribute("board", boardService.findById(number));

        UserSessionDto user = (UserSessionDto) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user.getNickname());
        }

        return "board/updateForm";
    }

    @PostMapping("/board/update/{number}")
    public String updateComplete(@PathVariable Long number, BoardUpdateRequestDto boardUpdateRequestDto, Model model){

        Board boardTmp = boardService.findById(number);

        boardTmp.setTitle(boardUpdateRequestDto.getTitle());
        boardTmp.setContent(boardUpdateRequestDto.getContent());
        boardTmp.setUpdateAt(LocalDate.now());

        boardService.boardRegistration(boardTmp);

        UserSessionDto user = (UserSessionDto) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user.getNickname());
        }

        return "redirect:../../board/content/" + number;

    }

    /**
     *  게시글 삭제
     */
    @GetMapping("/board/delete/{number}")
    public String boardDelete(@PathVariable("number") Long number, Model model){

        boardService.boardDelete(number);

        UserSessionDto user = (UserSessionDto) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user.getNickname());
        }

        return "redirect:../main";
    }

    /**
     *  게시글 검색
     */
    @GetMapping("/board/search")
    public String search(String keyword, Model model, @PageableDefault(size = 10, sort = "number", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<Board> searchedList = boardService.search(keyword, pageable);

        model.addAttribute("searchedList", searchedList);
        model.addAttribute("keyword", keyword);

        model.addAttribute("previous", pageable.previousOrFirst().getPageNumber());
        model.addAttribute("next", pageable.next().getPageNumber());
        model.addAttribute("hasNext", searchedList.hasNext());
        model.addAttribute("hasPrev", searchedList.hasPrevious());

        UserSessionDto user = (UserSessionDto) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user.getNickname());
        }

        return "board/search";

    }




//**********************회원 관련 정보**************************

    /**
     * 회원가입
     */
    @GetMapping("/auth/join")
    public String join(){
        return "user/user-join";
    }

    @PostMapping("/auth/joinProc")
    public String joinProc(@Valid UserDto userDto, Errors errors, Model model) {

        if (errors.hasErrors()) {
            //회원가입 실패 시 입력 값을 input 에 유지
            model.addAttribute("userDto", userDto);

            //유효성 통과 못한 필드/메시지 핸들링
            Map<String, String> validatorResult = userService.validateHandling(errors);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }

            return "user/user-join";
        }
        userService.join(userDto);
        return "redirect:main";
    }

    /**
     * 로그아웃
     */
    @GetMapping("/auth/logout")
    public String logout(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception{
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        UserSessionDto user = (UserSessionDto) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user.getNickname());
        }
        return "redirect:main";
    }

    /**
     *  회원 정보 수정페이지로 이동
     *  참고 : https://sas-study.tistory.com/m/302
     */
    @GetMapping("/auth/modify")
    public String modify(Model model, @LoginUser UserSessionDto userDto){
        if(userDto != null){
            model.addAttribute("user", userDto.getNickname());
            model.addAttribute("userDto", userDto);
        }
        UserSessionDto user = (UserSessionDto) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user.getNickname());
        }
        return "user/user-modify";
    }

    /**
     *  회원 정보 수정 (DB 적용)
     */
    @PutMapping("/auth/user")
    public ResponseEntity<String> modify(@RequestBody UserDto dto){
        userService.modify(dto);
        System.out.println(dto.getUsername());
        System.out.println(dto.getNickname());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

//********************** << 회원 관련 정보**************************





//********************** 댓글 **************************

    @PostMapping("/board/content/{number}/comments")
    public ResponseEntity commentSave(@PathVariable Long number, @RequestBody CommentRequestDto dto,
                                      @LoginUser UserSessionDto userSessionDto){
        return ResponseEntity.ok(commentService.commentSave(userSessionDto.getNickname(), number, dto));
    }

    @PutMapping("/board/content/{number}/comments/{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody CommentRequestDto dto){
        commentService.update(id, dto);
        return ResponseEntity.ok(id);
    }

    @PostMapping(value = "/board/content/{number}/comments/{id}")
    public String deleteComment(@PathVariable Long id){
        commentService.delete(id);
        return "redirect:content/{number}";
    }


//********************** << 댓글 **************************


}

