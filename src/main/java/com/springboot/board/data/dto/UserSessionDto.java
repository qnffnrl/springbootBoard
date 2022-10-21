/**
 * 인증된 사용자 정보를 세션에 저장하기 위한 클래스
 */

package com.springboot.board.data.dto;

import com.springboot.board.data.entity.Role;
import com.springboot.board.data.entity.User;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class UserSessionDto implements Serializable {

    private Long id;
    private String username;
    private String password;
    private String nickname;
    private String email;
    private Role role;
    private String modifiedDate;


    /**
     *  Entity -> DTO
     */
    public UserSessionDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.modifiedDate = user.getModifiedDate();
    }
}
