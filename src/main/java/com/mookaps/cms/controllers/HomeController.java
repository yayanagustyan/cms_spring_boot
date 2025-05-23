package com.mookaps.cms.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mookaps.cms.http.ApiResponse;

@RestController
@RequestMapping("/api/v1")
public class HomeController {

    @GetMapping("/dashboard")
    public ResponseEntity<?> getUsers() {
        Map<String, Object> data = new LinkedHashMap<>();
        String[] labels = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
        data.put("labels", labels);

        Integer[] datas = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        Integer[] datas2 = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

        for (int i = 0; i < 12; i++) {
            int random = ThreadLocalRandom.current().nextInt(100, 500);
            datas[i] = random;
        }

        for (int i = 0; i < 12; i++) {
            int random = ThreadLocalRandom.current().nextInt(10, 200);
            datas2[i] = random;
        }

        Map<String, Object> dt1 = new LinkedHashMap<>();
        dt1.put("title", "Visitor");
        dt1.put("value", datas);

        Map<String, Object> dt2 = new LinkedHashMap<>();
        dt2.put("title", "Revenue");
        dt2.put("value", datas2);

        List<Map<String, Object>> arr = new ArrayList<>();
        arr.add(dt1);
        arr.add(dt2);

        data.put("datas", arr);

        return ApiResponse.success(Collections.singletonList(data));
    }

}
