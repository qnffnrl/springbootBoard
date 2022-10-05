package com.springboot.board.data.dto;

import com.springboot.board.data.entity.Role;
import com.springboot.board.data.entity.User;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class UserSessionDto implements Serializable {

    private String username;
    private String password;
    private String nickname;
    private String email;
    private Role role;


    /**
     *  Entity -> DTO
     */
    public UserSessionDto(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.role = user.getRole();
    }
}
