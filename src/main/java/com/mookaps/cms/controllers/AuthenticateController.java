package com.mookaps.cms.controllers;

import com.mookaps.cms.helpers.Log;
import com.mookaps.cms.http.*;
import com.mookaps.cms.models.User;
import com.mookaps.cms.repository.UserRepository;
import com.mookaps.cms.security.JwtUtil;
import com.mookaps.cms.security.PasswordHasher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
public class AuthenticateController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private UserRepository repository;

    @PostMapping("/auth/authenticate")
    public ResponseEntity<?> generateToken(@RequestBody AuthRequest authRequest) throws Exception {
        String auth_email = authRequest.getEmail();
        String auth_pass = authRequest.getPassword();
        User user = repository.findByEmail(auth_email);
        if (user != null) {
            String db_pass = user.getPassword();
            Boolean cek = PasswordHasher.checkPassword(auth_pass, db_pass);
            if (cek) {
                // gunakan hashed pass dari DB => BCrypt harus pake hashed pass untuk auth
                // hash ini digunakan untuk pengecekan di file CustomUserDetailsService.java
                authManager.authenticate(new UsernamePasswordAuthenticationToken(auth_email, db_pass));
                String token = jwtUtil.generateToken(auth_email);
                user.setToken(token);
                repository.save(user); // to update token into DB

                AuthResponse res = new AuthResponse(auth_email, token);
                List<?> list = Collections.singletonList(res);
                Log.info(token);

                return ApiResponse.success(list, "Token Generated Successfully");
            } else {
                return ApiResponse.error(401, "Unauthorized :: Password not match");
            }
        } else {
            return ApiResponse.error(404, "User not found");
        }
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            jwtUtil.invalidateToken(token);
            return ApiResponse.success(Collections.emptyList(), "Logged out successfully");
        }
        return ApiResponse.error(404, "No token found");
    }

}
