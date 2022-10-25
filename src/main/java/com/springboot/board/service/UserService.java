package com.springboot.board.service;

import com.springboot.board.data.dto.UserDto;
import com.springboot.board.data.entity.User;
import com.springboot.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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
     *  save()가 없어도 저장이 된다. -> JPA의 영속성 컨텍스트 때문
     *  트랜잭션 안에서 DB의 데이터를 가져오면 이 데이터는 영속성 컨텍스트가 유지된 상태가 된다.
     *  이 상태에서 데이터 값을 변경하면 트랜잭션이 끝나는 시점에 변경된 데이터를 DB에 반영해준다.
     *
     *  (이런 개념을 변경 감지(더티 체킹)이라고 한다.)
     */
    @Transactional
    public void modify(UserDto dto){
        User user = userRepository.findById(dto.toEntity().getId()).orElseThrow(() ->
                new IllegalArgumentException("해당 회원이 존재하지 않습니다."));

        String encPassword = encoder.encode(dto.getPassword());
        user.modify(dto.getNickname(), encPassword);
    }
}














