package com.springboot.board;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class BoardApplication {


	public static void main(String[] args) {

		SpringApplication.run(BoardApplication.class, args);

	}

}
