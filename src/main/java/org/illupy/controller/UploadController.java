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

import java.io.File;
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
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File audio không được để trống");
        }

        String originalFilename = file.getOriginalFilename();
        String ext = (originalFilename != null && originalFilename.contains("."))
                ? originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase()
                : ".mp3";

        Path audioDir = Paths.get(uploadDir, "audio");
        Files.createDirectories(audioDir);

        String fileName = System.currentTimeMillis() + ext;
        Path destination = audioDir.resolve(fileName);

        file.transferTo(destination.toFile());

        String fileUrl = "/uploads/audio/" + fileName;
        return ResponseEntity.ok(ApiResponse.success(fileUrl));
    }
}