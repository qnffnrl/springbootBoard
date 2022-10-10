package com.springboot.board.service;

import com.springboot.board.data.dto.UserDto;
import com.springboot.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Transactional
    public Long join(UserDto dto){
        dto.setPassword(encoder.encode(dto.getPassword()));

        return userRepository.save(dto.toEntity()).getId();
    }

    /**
     * 회원가입 시 유효성 체크
     */
    @Transactional(readOnly = true)
    public Map<String, String> validateHandling(Errors errors) {

        Map<String, String> validatorResult = new HashMap<>();

        for (FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }
        return validatorResult;
    }

    /**
     *  id, pw, nickname 중복 체크
     */
    @Transactional(readOnly = true)
    public void checkUsernameDuplication(UserDto dto){
        boolean usernameDuplicate = userRepository.existsByUsername(dto.toEntity().getUsername());
        if (usernameDuplicate) {
            throw new IllegalStateException("아이디가 이미 존재합니다.");
        }
    }

    @Transactional(readOnly = true)
    public void checkNicknameDuplication(UserDto dto){
        boolean nicknameDuplicate = userRepository.existsByNickname(dto.toEntity().getNickname());
        if (nicknameDuplicate) {
            throw new IllegalStateException("닉네임이 이미 존재합니다.");
        }
    }

    @Transactional(readOnly = true)
    public void checkEmailDuplication(UserDto dto) {
        boolean emailDuplicate = userRepository.existsByEmail(dto.toEntity().getEmail());
        if (emailDuplicate) {
            throw new IllegalStateException("이메일이 이미 존재합니다.");
        }
    }
}














