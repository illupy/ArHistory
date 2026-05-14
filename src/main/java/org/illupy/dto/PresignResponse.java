package org.illupy.dto;

/**
 * Response for presigned upload URL.
 * signedUrl: FE dùng để PUT file trực tiếp lên Supabase.
 * publicUrl: URL CDN công khai sau khi upload xong (FE lưu cái này).
 */
public record PresignResponse(String signedUrl, String publicUrl) {}
