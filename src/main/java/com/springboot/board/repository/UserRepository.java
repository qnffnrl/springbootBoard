package com.springboot.board.repository;

import com.springboot.board.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     *  Spring Data JPA 에서 해당 데이터가 존재하는지 확인하기 위해 exist 를 사용한다
     *  해당 데이터가 존재하면 true, 없으면 false -> Return
     */
    boolean existsByUsername(String username);
    boolean existsByNickname(String nickname);
    boolean existsByEmail(String email);

    Optional<User> findByUsername(String username);

    User findByNickname(String nickname);

    /**
     * oAuth
     */
    Optional<User> findByEmail(String email);

}
