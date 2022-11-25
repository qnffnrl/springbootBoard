package com.springboot.board.configuration;

import com.springboot.board.auth.CustomOAuth2UserService;
import com.springboot.board.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * @EnableWebSecurity : 본 클래스가 Spring Security 설정 클래스임을 알려줌
 * @EnableGlobalMethodSecurity : 특정 주소로 접근하면 권한 및 인증을 미리 체크
 */
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomUserDetailsService customUserDetailsService;
    private final AuthenticationFailureHandler customFailureHandler;
    private final CustomOAuth2UserService customOAuth2UserService;


    /**
     *   BCryptPasswordEncoder : Spring Security 에서 제공하는 비밂번호 암호화 객체
     */
    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     *  AuthenticationManager Bean 등록
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(encoder());
    }

    /**
     *  인증을 무시할 경로 지정
     *  static 하위 폴더들은 인증없이 접근되야 되기 때문에 인증을 무시하도록 지정함
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers( "/css/**", "/js/**", "/img/**");
    }

    /**
     *  HttpSecurity 를 통해 HTTP 요청에 대한 보안을 설정함
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                /**
                 * Spring Security CSRF 설정
                 * Spring Security 는 로그인 form 전송 시 CSRF 토큰을 전송해야 함
                 * 근데 머스테치는 CSRF 토큰을 제공해주지 않음
                 */

                // 해당 경로의 URL은 CSRF 보호에서 제외
                .csrf().ignoringAntMatchers("/board/**", "/auth/user", "/oauth2/**")

                .and()

                /**
                 * HttpServletRequest 에 따라 접근을 제한함
                 */
                .authorizeRequests()
                .antMatchers("/auth/**", "/board/main", "/board/content/**", "/board/search", "/oauth2/**")//해당 경로에 대해 인증없이 접근가능
                .permitAll() // 권한에 다른 접근을 설정함
                .anyRequest().authenticated() //그 외의 경로는 인증이 필요함

                .and()

                /**
                 * form 기반으로 인증함
                 * /login 경로로 접근하면 Spring Security 에서 제공하는 Form 이 나옴
                 */
                .formLogin()
                .loginProcessingUrl("/auth/loginProc") // Spring Security 에서 해당 주소로 오는 요청을 낚아채 로직 수행
                .defaultSuccessUrl("/board/main") // 로그인 성공 시 이동하는 페이지
                .failureHandler(customFailureHandler)

                .and()

                /**
                 * 로그아웃을 지원하는 메서드
                 * /logout 에 접근하면 세션을 제거해줌
                 * 기본적으로 WebSecurityConfigureAdapter 를 사용하면 자동으로 적용됨 
                 */

                .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout")) // Spring Security 에서 해당 주소로 오는 요청을 낚아채 로직 수행
                    .invalidateHttpSession(true).deleteCookies("user") // HTTP 세션을 초기화
                    .logoutSuccessUrl("/board/main") // 로그아웃 시 이동되는 페이지

                .and()

                /**
                 * oAuth
                 */
                .oauth2Login()
                    .defaultSuccessUrl("/board/main")
                    .userInfoEndpoint()
                    .userService(customOAuth2UserService);
    }
}