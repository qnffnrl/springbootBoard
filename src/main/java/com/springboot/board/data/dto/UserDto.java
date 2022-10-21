package com.springboot.board.data.dto;

import com.springboot.board.data.entity.Role;
import com.springboot.board.data.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private Long id;

    @NotBlank(message = "아이디를 입력하세요.")
    private String username;

    @NotBlank(message = "비밀번호를 입력하세요")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*\\W)(?=\\S+$).{6,16}",
            message = "비밀번호는 6~16자, 영문 소문자, 숫자, 특수문자를 사용하세요.")
    private String password;

    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$", message = "닉네임은 특수문자를 제외한 2~10자리여야 합니다.")
    private String nickname;

    @Pattern(regexp = "^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$", message = "이메일 형식이 올바르지 않습니다.")
    @NotBlank(message = "이메일을 입력하세요.")
    private String email;

    private Role role;

    /**
     * DTO -> Entity
     */
    public User toEntity(){
        User user = User.builder()
                .id(id)
                .username(username)
                .password(password)
                .nickname(nickname)
                .email(email)
                .role(role.USER)
                .build();
        return user;
    }

}
