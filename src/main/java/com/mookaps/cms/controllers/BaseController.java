package com.mookaps.cms.controllers;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.SpringBootVersion;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.mookaps.cms.http.ApiResponse;

@Controller
public class BaseController {

    @GetMapping("/")
    public ResponseEntity<?> index() {
        Map<String, Object> data = new HashMap<>();
        data.put("version", "1.0.0");
        data.put("engine", "Spring Boot " + SpringBootVersion.getVersion());

        return ApiResponse.json(200, "Welcome to API", Collections.singletonList(data));
    }

    @GetMapping("/docs")
    public String doc(Model model) {
        model.addAttribute("data", "This page is for documentations");

        List<String> users = List.of("Alice", "Bob", "Charlie");
        model.addAttribute("users", users);

        return "doc";
    }

}
