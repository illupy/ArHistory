package org.illupy.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.illupy.dto.PresignResponse;
import org.illupy.service.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * Supabase Storage implementation.
 * Active when storage.type=supabase.
 *
 * Files are stored in a single public bucket.
 * Returned URL is the public Supabase CDN URL.
 */
@Service
@Slf4j
@ConditionalOnProperty(name = "storage.type", havingValue = "supabase")
public class SupabaseStorageService implements StorageService {

    // e.g. https://gfualrlzqenuressyeli.supabase.co
    @Value("${supabase.url}")
    private String supabaseUrl;

    // Service-role key from Supabase Dashboard > Settings > API
    @Value("${supabase.service-key}")
    private String serviceKey;

    // Bucket name (will be auto-created as public on first startup)
    @Value("${supabase.storage.bucket:ar-history-assets}")
    private String bucket;

    private final RestTemplate restTemplate = new RestTemplate();

    // ---------------------------------------------------------------
    // Bucket auto-creation
    // ---------------------------------------------------------------

    @PostConstruct
    public void ensureBucketExists() {
        String bucketUrl = supabaseUrl + "/storage/v1/bucket";

        HttpHeaders headers = buildJsonHeaders();
        Map<String, Object> body = Map.of(
                "id", bucket,
                "name", bucket,
                "public", true
        );

        try {
            restTemplate.exchange(
                    bucketUrl,
                    HttpMethod.POST,
                    new HttpEntity<>(body, headers),
                    String.class
            );
            log.info("Supabase bucket '{}' created successfully.", bucket);
        } catch (HttpClientErrorException e) {
            String responseBody = e.getResponseBodyAsString();
            // Supabase trả HTTP 400 với body {"statusCode":"409","error":"Duplicate",...}
            // khi bucket đã tồn tại — coi đây là trường hợp bình thường
            if (e.getStatusCode() == HttpStatus.CONFLICT
                    || responseBody.contains("409")
                    || responseBody.contains("Duplicate")
                    || responseBody.contains("already exists")) {
                log.info("Supabase bucket '{}' already exists — skipping creation.", bucket);
            } else {
                log.error("Failed to create Supabase bucket '{}': {} — {}", bucket, e.getStatusCode(), responseBody);
                throw new RuntimeException("Cannot ensure Supabase bucket exists", e);
            }
        }
    }

    // ---------------------------------------------------------------
    // StorageService implementation
    // ---------------------------------------------------------------

    @Override
    public String store(MultipartFile file, String subDir) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File không được để trống");
        }

        try {
            String ext = extractExtension(file.getOriginalFilename());
            String objectPath = subDir + "/" + System.currentTimeMillis()
                    + "_" + (int) (Math.random() * 10000) + ext;

            String uploadUrl = supabaseUrl + "/storage/v1/object/" + bucket + "/" + objectPath;

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(serviceKey);
            headers.setContentType(resolveContentType(file.getContentType(), ext));
            // Overwrite if same name exists
            headers.set("x-upsert", "true");

            HttpEntity<byte[]> entity = new HttpEntity<>(file.getBytes(), headers);
            restTemplate.exchange(uploadUrl, HttpMethod.POST, entity, String.class);

            String publicUrl = supabaseUrl + "/storage/v1/object/public/" + bucket + "/" + objectPath;
            log.info("Uploaded to Supabase: {}", publicUrl);
            return publicUrl;

        } catch (IOException e) {
            throw new RuntimeException("Đọc file thất bại: " + e.getMessage(), e);
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Upload Supabase thất bại: " + e.getResponseBodyAsString(), e);
        }
    }

    @Override
    public void delete(String fileUrl) {
        if (fileUrl == null || fileUrl.isBlank()) return;

        // Extract object path from URL:
        // https://<ref>.supabase.co/storage/v1/object/public/<bucket>/<objectPath>
        String marker = "/storage/v1/object/public/" + bucket + "/";
        int idx = fileUrl.indexOf(marker);
        if (idx == -1) {
            log.warn("Không thể xác định object path từ URL: {}", fileUrl);
            return;
        }
        String objectPath = fileUrl.substring(idx + marker.length());

        String deleteUrl = supabaseUrl + "/storage/v1/object/" + bucket + "/" + objectPath;

        try {
            HttpHeaders headers = buildJsonHeaders();
            restTemplate.exchange(deleteUrl, HttpMethod.DELETE, new HttpEntity<>(headers), String.class);
            log.info("Deleted from Supabase: {}", objectPath);
        } catch (HttpClientErrorException e) {
            log.warn("Xóa file Supabase thất bại ({}): {}", objectPath, e.getMessage());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public PresignResponse presign(String subDir, String filename) {
        String ext = extractExtension(filename);
        String objectPath = subDir + "/" + System.currentTimeMillis()
                + "_" + (int) (Math.random() * 10000) + ext;

        // Ask Supabase to create a signed upload URL
        String signEndpoint = supabaseUrl + "/storage/v1/object/upload/sign/" + bucket + "/" + objectPath;
        HttpHeaders headers = buildJsonHeaders();
        Map<String, Object> body = Map.of("upsert", true);

        try {
            ResponseEntity<Map> resp = restTemplate.exchange(
                    signEndpoint, HttpMethod.POST, new HttpEntity<>(body, headers), Map.class);

            // Supabase trả về { "url": "...", "token": "...", "path": "..." }
            // Không dùng "url" vì một số version thiếu prefix /storage/v1/
            // → tự build URL đúng từ token
            String token = (String) resp.getBody().get("token");
            String signedUrl = supabaseUrl
                    + "/storage/v1/object/upload/sign/" + bucket + "/" + objectPath
                    + "?token=" + token;
            String publicUrl = supabaseUrl + "/storage/v1/object/public/" + bucket + "/" + objectPath;

            log.info("Presigned URL created for: {}", objectPath);
            return new PresignResponse(signedUrl, publicUrl);
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Tạo presigned URL thất bại: " + e.getResponseBodyAsString(), e);
        }
    }

    // ---------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------

    private HttpHeaders buildJsonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(serviceKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private String extractExtension(String filename) {
        if (filename != null && filename.contains(".")) {
            return filename.substring(filename.lastIndexOf(".")).toLowerCase();
        }
        return "";
    }

    private MediaType resolveContentType(String contentType, String ext) {
        if (contentType != null && !contentType.isBlank()) {
            try {
                return MediaType.parseMediaType(contentType);
            } catch (Exception ignored) {}
        }
        return switch (ext) {
            case ".png"  -> MediaType.IMAGE_PNG;
            case ".gif"  -> MediaType.IMAGE_GIF;
            case ".webp" -> MediaType.parseMediaType("image/webp");
            case ".mp4"  -> MediaType.parseMediaType("video/mp4");
            case ".mp3"  -> MediaType.parseMediaType("audio/mpeg");
            case ".wav"  -> MediaType.parseMediaType("audio/wav");
            case ".pdf"  -> MediaType.APPLICATION_PDF;
            default      -> MediaType.IMAGE_JPEG;
        };
    }
}
