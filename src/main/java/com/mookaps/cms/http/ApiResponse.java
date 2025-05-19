package com.mookaps.cms.http;

import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.MediaType;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ApiResponse {

    public static ResponseEntity<?> success(List<?> data) {
        return success(data, "OK");
    }

    public static ResponseEntity<?> success(List<?> data, String message) {
        JsonResponse<?> json = new JsonResponse<>(200, message, data.size(), data);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(json);
    }

    public static ResponseEntity<?> error(int code, String message) {
        List<?> list = Collections.emptyList();
        JsonResponse<?> json = new JsonResponse<>(code, message, list.size(), list);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(json);
    }

    public static ResponseEntity<?> json(int status, String message, List<Map<String, Object>> data) {
        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("status", status);
        map.put("message", message);
        map.put("count", data.size());
        map.put("data", data);

        String json = "";
        try {
            json = mapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(json);
    }

}
