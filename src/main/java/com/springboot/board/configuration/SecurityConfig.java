package com.springboot.board.configuration;

import com.springboot.board.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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


    /**
     *   BCryptPasswordEncoder : Spring Security 에서 제공하는 비밂번호 암호화 객체
     */
    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
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
                 * Spring Security 는 CSRF 토근이 없으면 해당 요청을 막아서 잠시 비활성화
                 */
                .csrf().disable()

                /**
                 * HttpServletRequest 에 따라 접근을 제한함
                 */
                .authorizeRequests()
                    .antMatchers("/auth/**", "/board/main")//해당 경로에 대해 인증없이 접근가능
                    .permitAll() // 권한에 다른 접근을 설정함
                    .anyRequest().authenticated() //그 외의 경로는 인증이 필요함
                
                .and()

                /**
                 * form 기반으로 인증함
                 * /login 경로로 접근하면 Spring Security 에서 제공하는 Form 이 나옴
                 */
                .formLogin()
                    .loginPage("/auth/login") // 기본 제공 Form 말고 커스텀 로그인 폼을 쓸때 사용
                    .loginProcessingUrl("/auth/loginProc") // Spring Security 에서 해당 주소로 오는 요청을 낚아채 로직 수행
                    .defaultSuccessUrl("/board/main") // 로그인 성공 시 이동하는 페이지

                .and()

                /**
                 * 로그아웃을 지원하는 메서드
                 * /logout 에 접근하면 세션을 제거해줌
                 * 기본적으로 WebSecurityConfigureAdapter 를 사용하면 자동으로 적용됨 
                 */
                .logout()
                    .logoutUrl("/auth/logout") // Spring Security 에서 해당 주소로 오는 요청을 낚아채 로직 수행
                    .logoutSuccessUrl("/auth/login") // 로그아웃 시 이동되는 페이지
                    .invalidateHttpSession(true); // HTTP 세션을 초기화
    }
}