package com.mookaps.cms.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.mookaps.cms.files.UploadRequest;
import com.mookaps.cms.helpers.Common;
import com.mookaps.cms.http.ApiResponse;
import com.mookaps.cms.services.FileStorageService;

import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class UploadController {

    @Autowired
    private FileStorageService fileStorageService;

    public boolean isValidExtension(String fileName) {
        String extension = getFileExtension(fileName).toLowerCase();
        return List.of("jpg", "jpeg", "png", "gif", "pdf").contains(extension);
    }

    public String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains("."))
            return "";
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }

    // Upload file
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(HttpServletRequest request, @RequestParam("file") MultipartFile file,
            @RequestParam("path") String path) {
        try {
            String name = file.getOriginalFilename();
            if (isValidExtension(name)) {
                String res = fileStorageService.storeFile(file, path);

                Map<String, Object> data = new HashMap<>();
                data.put("filename", res);
                data.put("link", Common.getBaseUrl(request) + "/api/v1/download/" + path + "/" + res);
                return ApiResponse.success(Collections.singletonList(data), "File uploaded");
            } else {
                return ApiResponse.error(400, "Invalid file type");
            }
        } catch (IOException e) {
            return ApiResponse.error(400, "Upload failed :: " + e.getMessage());
        }
    }

    @PostMapping("/upload/base64")
    public ResponseEntity<?> uploadBase64File(HttpServletRequest request, @RequestBody UploadRequest req) {
        try {
            if (!req.getFilename().matches(".*\\.(jpg|png|jpeg|pdf)$")) {
                return ApiResponse.error(400, "Unsupported file type.");
            } else {
                String fileName = fileStorageService.resizeBase64Image(req);
                Map<String, Object> data = new HashMap<>();
                data.put("filename", fileName);
                data.put("link", Common.getBaseUrl(request) + "/api/v1/download/" + req.getPath() + "/" + fileName);
                return ApiResponse.success(Collections.singletonList(data), "File uploaded");
            }
        } catch (IllegalArgumentException e) {
            return ApiResponse.error(400, "Invalid base64 content.");
        } catch (IOException e) {
            return ApiResponse.error(500, "File save failed.");
        }
    }

    // Download file
    @GetMapping("/download/{folder}/{subfolder}/{filename}")
    public ResponseEntity<?> downloadFile(@PathVariable String folder, @PathVariable String subfolder,
            @PathVariable String filename) {
        return downloadFile(folder + "/" + subfolder + "/" + filename);
    }

    @GetMapping("/download/{folder}/{filename}")
    public ResponseEntity<?> downloadFile(@PathVariable String folder, @PathVariable String filename) {
        return downloadFile(folder + "/" + filename);
    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<?> downloadFile(@PathVariable String filename) {
        try {
            Resource resource = fileStorageService.loadFile(filename);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (IOException e) {
            return ApiResponse.error(404, "File Not Found");
        }
    }
}
