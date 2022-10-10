package com.springboot.board.validator;


import com.springboot.board.data.dto.UserDto;
import com.springboot.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@RequiredArgsConstructor
@Component
public class CheckUsernameValidator extends AbstractValidator<UserDto> {

    private final UserRepository userRepository;

    @Override
    protected void doValidate(UserDto dto, Errors errors) {
        if (userRepository.existsByUsername(dto.toEntity().getUsername())) {
            errors.rejectValue("username", "아아디 중복 오류", "이미 사용중인 아이디 입니다.");
        }
    }
}
