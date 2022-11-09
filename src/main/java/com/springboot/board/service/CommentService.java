package com.springboot.board.service;

import com.springboot.board.data.dto.CommentRequestDto;
import com.springboot.board.data.entity.Board;
import com.springboot.board.data.entity.Comment;
import com.springboot.board.data.entity.User;
import com.springboot.board.repository.BoardRepository;
import com.springboot.board.repository.CommentRepository;
import com.springboot.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public Long commentSave(String nickname, Long id, CommentRequestDto dto) {

        User user = userRepository.findByNickname(nickname);
        Board board = boardRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Comment Write Fail : That Post doesn't exist!!"));

        dto.setUser(user);
        dto.setBoard(board);

        Comment comment = dto.toEntity();
        commentRepository.save(comment);

        return dto.getId();
    }

}








