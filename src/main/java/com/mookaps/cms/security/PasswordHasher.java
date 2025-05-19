package com.mookaps.cms.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.mookaps.cms.helpers.Log;

public class PasswordHasher {

    public static String hashPassword(String rawPassword) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(rawPassword);
    }

    public static Boolean checkPassword(CharSequence rawPassword, String hashed) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, hashed);
    }

    public static void main(String[] args) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashed = passwordEncoder.encode("password");
        Log.info("Hashed password: " + hashed);
    }
}
