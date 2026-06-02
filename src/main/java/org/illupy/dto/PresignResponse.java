package org.illupy.dto;

/**
 * Response for presigned upload URL.
 * signedUrl: FE dùng để PUT file trực tiếp lên Supabase.
 * publicUrl: URL CDN công khai sau khi upload xong (FE lưu cái này).
 * token: Signed upload token — FE truyền vào header x-signature khi dùng TUS resumable upload.
 * bucketName: Tên bucket — FE truyền vào TUS metadata.
 * objectName: Đường dẫn object trong bucket — FE truyền vào TUS metadata.
 */
public record PresignResponse(String signedUrl, String publicUrl, String token, String bucketName, String objectName) {}
