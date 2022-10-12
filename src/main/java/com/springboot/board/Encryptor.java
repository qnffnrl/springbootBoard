package com.springboot.board;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

public class Encryptor {

    public static void main(String[] args) {

        String password = "";

        StandardPBEStringEncryptor jasypt = new StandardPBEStringEncryptor();
        jasypt.setPassword("garbage");
        jasypt.setAlgorithm("PBEWITHMD5ANDDES");

        String en = jasypt.encrypt(password);
        String de = jasypt.decrypt(en);

        System.out.println("Encryption = " + en);
        System.out.println("Decryption = " + de);

    }



}
