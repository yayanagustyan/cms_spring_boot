package com.mookaps.cms.services;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mookaps.cms.files.UploadRequest;
import com.mookaps.cms.helpers.Log;

import java.io.*;
import java.nio.file.*;
import java.util.Base64;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    private String uploadDir = "uploads";

    public FileStorageService() throws IOException {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(this.fileStorageLocation);
    }

    private String createDir(String path) throws IOException {
        if (path != null) {
            Path subDirPath = Paths.get(uploadDir, path).toAbsolutePath().normalize();
            if (!Files.exists(subDirPath)) {
                Files.createDirectories(subDirPath);
            }
            return subDirPath.toString() + "/";
        }
        return uploadDir + "/";
    }

    public String storeFile(MultipartFile file) throws IOException {
        Path targetLocation = fileStorageLocation.resolve(file.getOriginalFilename());
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        return file.getOriginalFilename();
    }

    public String storeBase64(UploadRequest file) throws IOException {
        String dirsave = createDir(file.getPath());
        String filePath = dirsave + file.getFilename();

        // Decode Base64 string
        byte[] fileBytes = Base64.getDecoder().decode(file.getBase64());
        try {
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                fos.write(fileBytes);
            }
        } catch (Exception e) {
            Log.error(e.getMessage());
            return e.getMessage();
        }
        return file.getFilename();
    }

    public Resource loadFile(String filename) throws IOException {
        Path filePath = fileStorageLocation.resolve(filename).normalize();
        if (!Files.exists(filePath))
            throw new FileNotFoundException("File not found");
        return new org.springframework.core.io.UrlResource(filePath.toUri());
    }
}
