package com.springboot.board.service;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class CustomAuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException{

        String errorMessage;
        if(exception instanceof BadCredentialsException) {
            errorMessage = "ID or Password did not match! Please Check Again";
        }else if(exception instanceof InternalAuthenticationServiceException) {
            errorMessage = "Internal ERROR";
        }else if(exception instanceof UsernameNotFoundException){
            errorMessage = "No id! Please Sign Up";
        } else if (exception instanceof AuthenticationCredentialsNotFoundException) {
            errorMessage = "Auth Request was Dined";
        } else {
            errorMessage = "Login Failure, Cause Unknown Error";
        }
        errorMessage = URLEncoder.encode(errorMessage, StandardCharsets.UTF_8);
        setDefaultFailureUrl("/auth/login?error=true&exception=" + errorMessage);

        super.onAuthenticationFailure(request, response, exception);
    }

}
