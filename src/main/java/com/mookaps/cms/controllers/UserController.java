package com.mookaps.cms.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mookaps.cms.models.User;
import com.mookaps.cms.services.UserService;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping("/users")
    public ResponseEntity<?> getUsers() {
        return service.getUsers();
    }

    @GetMapping("/users/{id:[0-9]+}")
    public ResponseEntity<?> getUserId(@PathVariable int id) {
        return service.getUserId(id);
    }

    @PostMapping("/users/search")
    public ResponseEntity<?> searchUsers(@RequestBody Map<String, Object> body) {
        String keys = body.get("keyword").toString();
        return service.searchUser(keys);
    }

    @PostMapping("/users/save")
    public ResponseEntity<?> saveUsers(@RequestBody User user) {
        return service.saveUser(user);
    }

    @DeleteMapping("/users/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id) {
        return service.deleteUser(id);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMe() {
        return service.getMe();
    }

}
