package org.illupy.controller;

import lombok.RequiredArgsConstructor;
import org.illupy.common.ApiResponse;
import org.illupy.dto.PresignResponse;
import org.illupy.service.StorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/uploads")
@RequiredArgsConstructor
public class UploadController {

    private final StorageService storageService;

    // ---------------------------------------------------------------
    // Presigned URL (recommended) — FE uploads directly to Supabase
    // POST /api/uploads/presign  body: { subDir, filename }
    // Returns: { signedUrl, publicUrl }
    // ---------------------------------------------------------------

    @PostMapping("/presign")
    public ResponseEntity<ApiResponse<PresignResponse>> presign(@RequestBody Map<String, String> body) {
        String subDir   = body.getOrDefault("subDir", "images");
        String filename = body.getOrDefault("filename", "file");
        PresignResponse resp = storageService.presign(subDir, filename);
        return ResponseEntity.ok(ApiResponse.success(resp));
    }

    // ---------------------------------------------------------------
    // Legacy server-side upload (kept as fallback)
    // ---------------------------------------------------------------

    @PostMapping("/image")
    public ResponseEntity<ApiResponse<String>> uploadImage(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(ApiResponse.success(storageService.store(file, "images")));
    }

    @PostMapping("/audio")
    public ResponseEntity<ApiResponse<String>> uploadAudio(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(ApiResponse.success(storageService.store(file, "audio")));
    }

    @PostMapping("/video")
    public ResponseEntity<ApiResponse<String>> uploadVideo(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(ApiResponse.success(storageService.store(file, "videos")));
    }

    @PostMapping("/images")
    public ResponseEntity<ApiResponse<List<String>>> uploadMultipleImages(@RequestParam("files") MultipartFile[] files) {
        if (files.length > 4) throw new IllegalArgumentException("Tối đa 4 ảnh");
        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) urls.add(storageService.store(file, "images"));
        return ResponseEntity.ok(ApiResponse.success(urls));
    }
}