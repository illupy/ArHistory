package org.illupy.controller;

import lombok.RequiredArgsConstructor;
import org.illupy.common.ApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/uploads")
@RequiredArgsConstructor
public class UploadController {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @PostMapping("/audio")
    public ResponseEntity<ApiResponse<String>> uploadAudio(@RequestParam("file") MultipartFile file) throws IOException {
        String fileUrl = saveFile(file, "audio", ".mp3");
        return ResponseEntity.ok(ApiResponse.success(fileUrl));
    }

    @PostMapping("/image")
    public ResponseEntity<ApiResponse<String>> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        String fileUrl = saveFile(file, "images", ".png");
        return ResponseEntity.ok(ApiResponse.success(fileUrl));
    }

    private String saveFile(MultipartFile file, String subDir, String defaultExt) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File không được để trống");
        }

        String originalFilename = file.getOriginalFilename();
        String ext = (originalFilename != null && originalFilename.contains("."))
                ? originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase()
                : defaultExt;

        Path targetDir = Paths.get(uploadDir, subDir);
        Files.createDirectories(targetDir);

        String fileName = System.currentTimeMillis() + ext;
        Path destination = targetDir.resolve(fileName);
        file.transferTo(destination.toFile());

        return "/uploads/" + subDir + "/" + fileName;
    }
}