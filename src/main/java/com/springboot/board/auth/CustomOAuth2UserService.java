package com.springboot.board.auth;

import com.springboot.board.data.dto.UserSessionDto;
import com.springboot.board.data.entity.User;
import com.springboot.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final HttpSession session;

    /**
     *  플랫폼 별로 구분 하기 위해 본 클래스 선언
     */

        @Override
        public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

            // DefaultOAuth2User 서비스를 통해 User 정보를 가져와야 하기 때문에 대리자 생성
            OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
            OAuth2User oAuth2User = delegate.loadUser(userRequest);

            // OAuth2 서비스 id 구분 코드 (구글, 네이버 etc)
            String registrationId = userRequest.getClientRegistration().getRegistrationId();

            // OAuth2 로그인 진행시 키가 되는 필드 값(PK)
            String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        // OAuth2UserService 를 통해 가져온 데이터를 담을 클래스
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        // 로그인한 유저 정보
        User user = saveOrUpdate(attributes);

        session.setAttribute("user", new UserSessionDto(user));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleValue())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }

    // user 가 이미 저장되어 있으면 업데이트
    private User saveOrUpdate(OAuthAttributes attributes) {

        User user = userRepository.findByEmail(attributes.getEmail())
                .map(User::updateModifiedDate)
                .orElse(attributes.toEntity());

        return userRepository.save(user);
    }

}
