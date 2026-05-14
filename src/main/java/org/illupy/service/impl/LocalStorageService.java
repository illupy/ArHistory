package org.illupy.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.illupy.service.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Local filesystem storage implementation.
 * Active when storage.type=local (default).
 */
@Service
@Slf4j
@ConditionalOnProperty(name = "storage.type", havingValue = "local", matchIfMissing = true)
public class LocalStorageService implements StorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public String store(MultipartFile file, String subDir) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File không được để trống");
        }

        try {
            String originalFilename = file.getOriginalFilename();
            String ext = (originalFilename != null && originalFilename.contains("."))
                    ? originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase()
                    : "";

            Path targetDir = Paths.get(uploadDir, subDir);
            Files.createDirectories(targetDir);

            String fileName = System.currentTimeMillis() + "_" + (int)(Math.random() * 10000) + ext;
            Path destination = targetDir.resolve(fileName);
            file.transferTo(destination.toFile());

            log.info("Stored file: {}/{}", subDir, fileName);
            return "/uploads/" + subDir + "/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Lưu file thất bại: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(String fileUrl) {
        if (fileUrl == null || !fileUrl.startsWith("/uploads/")) return;
        try {
            String relativePath = fileUrl.substring("/uploads/".length());
            Path filePath = Paths.get(uploadDir, relativePath);
            Files.deleteIfExists(filePath);
            log.info("Deleted file: {}", fileUrl);
        } catch (IOException e) {
            log.warn("Could not delete file {}: {}", fileUrl, e.getMessage());
        }
    }
}
