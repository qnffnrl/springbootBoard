package com.springboot.board.data.entity;

import lombok.*;

import javax.persistence.*;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class User extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 아이디
    @Column(nullable = false, length = 30, unique = true)
    private String username;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 50)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    /**
     *  회원 정보 수정
     */
    public void modify(String nickname, String password){
        this.nickname = nickname;
        this.password = password;
    }

    /**
     *  소셜 로그인 시 이미 등록된 회원이면 수정날짜만 업데이트
     *  기존 데이터는 유지
     */
    public User updateModifiedDate(){
        this.onPreUpdate();
        return this;
    }

    public String getRoleValue(){
        return this.role.getValue();
    }

}
