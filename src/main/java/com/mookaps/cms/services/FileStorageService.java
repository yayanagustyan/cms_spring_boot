package com.mookaps.cms.services;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mookaps.cms.files.UploadRequest;
import com.mookaps.cms.helpers.Common;
import com.mookaps.cms.helpers.Log;

import net.coobird.thumbnailator.Thumbnails;

import java.io.*;
import java.nio.file.*;
import java.util.Base64;
import java.util.Optional;

@Service
public class FileStorageService {

    private int height = 500;
    private int width = 500;

    private final Path fileStorageLocation;

    private String uploadDir = "uploads";

    public FileStorageService() throws IOException {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(this.fileStorageLocation);
    }

    private String getExt(MultipartFile file) {
        return Optional.ofNullable(file)
                .map(MultipartFile::getContentType)
                .orElse("");
    }

    private void deleteAllContain(Path directory) {
        try {
            Files.list(directory)
                    .filter(Files::isRegularFile)
                    .forEach(file -> {
                        try {
                            Files.delete(file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public String storeFile(MultipartFile file, String path) throws IOException {
        String dirSave = createDir(path);
        Path uploadPath = Paths.get(dirSave);

        deleteAllContain(uploadPath);

        String ext = getExt(file).split("/")[1];
        String filename = "IMG-" + Common.generateRandomString(20) + "." + ext;

        try (InputStream in = file.getInputStream()) {
            Thumbnails.of(in)
                    .size(width, height)
                    .keepAspectRatio(true)
                    .outputFormat(ext)
                    .toFile(dirSave + filename);
        } catch (Exception e) {
            e.getStackTrace();
        }
        return filename;
    }

    public String storeBase64(UploadRequest file) throws IOException {
        String dirSave = createDir(file.getPath());
        String ext = file.getFilename().split("\\.")[1];
        String filename = "IMG-" + Common.generateRandomString(12) + "." + ext;
        String filePath = dirSave + filename;

        Path uploadPath = Paths.get(dirSave);
        deleteAllContain(uploadPath);

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
        return filename;
    }

    public String resizeBase64Image(UploadRequest file) throws IOException {
        String base64Image = file.getBase64();
        String dirSave = createDir(file.getPath());
        String ext = file.getFilename().split("\\.")[1];
        String filename = "IMG-" + Common.generateRandomString(20) + "." + ext;
        String filePath = dirSave + filename;

        Path uploadPath = Paths.get(dirSave);
        deleteAllContain(uploadPath);

        // 1. Decode base64 to BufferedImage
        byte[] decodedBytes = Base64.getDecoder().decode(base64Image);
        InputStream inputStream = new ByteArrayInputStream(decodedBytes);

        // 2. Resize image with Thumbnailator
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Thumbnails.of(inputStream)
                .size(width, height)
                .keepAspectRatio(true)
                .outputFormat(ext) // or "png"
                .toOutputStream(outputStream);

        // 3. Save it
        byte[] resizedBytes = outputStream.toByteArray();
        try {
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                fos.write(resizedBytes);
            }
        } catch (Exception e) {
            Log.error(e.getMessage());
            return e.getMessage();
        }
        return filename;

    }

    public Resource loadFile(String filename) throws IOException {
        Path filePath = fileStorageLocation.resolve(filename).normalize();
        if (!Files.exists(filePath))
            throw new FileNotFoundException("File not found");
        return new org.springframework.core.io.UrlResource(filePath.toUri());
    }
}
