package com.mookaps.cms.services;

import com.mookaps.cms.helpers.Common;
import com.mookaps.cms.helpers.Log;
import com.mookaps.cms.http.ApiResponse;
import com.mookaps.cms.models.User;
import com.mookaps.cms.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<?> getUsers() {
        List<User> emps = userRepository.findAll();
        return ApiResponse.success(emps);
    }

    public ResponseEntity<?> searchUser(String keyword) {
        List<User> emps = userRepository.findByKeyword(keyword);
        return ApiResponse.success(emps);
    }

    public ResponseEntity<?> saveUser(User user) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        User usr = userRepository.findByEmail(user.getEmail());
        if (usr != null) {
            // EDIT
            String savedPass = usr.getPassword();
            usr.setName(user.getName() != null ? user.getName() : usr.getName());
            usr.setEmail(user.getEmail() != null ? user.getEmail() : usr.getEmail());

            usr.setPhone_number(user.getPhone_number() != null ? user.getPhone_number() : usr.getPhone_number());
            usr.setPhoto_1(user.getPhoto_1() != null ? user.getPhoto_1() : usr.getPhoto_1());
            usr.setPhoto_2(user.getPhoto_2() != null ? user.getPhoto_2() : usr.getPhoto_2());
            usr.setGender(user.getGender() != null ? user.getGender() : usr.getGender());
            usr.setAddress(user.getAddress() != null ? user.getAddress() : usr.getAddress());
            usr.setActive(user.getActive() != null ? user.getActive() : usr.getActive());
            usr.setStatus(user.getStatus() != null ? user.getStatus() : usr.getStatus());
            usr.setLevel(user.getLevel() != null ? user.getLevel() : usr.getLevel());

            usr.setUpdated_at(Common.currentDateTime());

            if (user.getPassword() != null) {
                String enc_password = bCryptPasswordEncoder.encode(user.getPassword());
                usr.setPassword(enc_password);
            } else {
                usr.setPassword(savedPass);
            }
        } else {
            Log.info("PASSWORD :: " + user.getPassword());
            usr = userRepository.save(user);
            if (user.getPassword() != null) {
                String enc_password = bCryptPasswordEncoder.encode(user.getPassword());
                usr.setPassword(enc_password);
            }
            usr.setCreated_at(Common.currentDateTime());
        }
        return ApiResponse.success(Collections.singletonList(usr));
    }

    public ResponseEntity<?> getUserId(int id) {
        Optional<User> user = userRepository.findById(id);
        List<?> list = user.isPresent() ? Collections.singletonList(user) : Collections.emptyList();
        return ApiResponse.success(list);
    }

    public ResponseEntity<?> deleteUser(int id) {
        if (!userRepository.existsById(id)) {
            return ApiResponse.error(404, "No Data To Delete");
        } else {
            userRepository.deleteById(id);
            return ApiResponse.success(Collections.emptyList(), "Data deleted successfully");
        }

    }

    public ResponseEntity<?> getMe() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // this is the username from JWT

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("username", username);
        map.put("raws", authentication);
        // map.put("active", 1);
        return ApiResponse.json(200, "Login Information", Collections.singletonList(map));
    }

}
