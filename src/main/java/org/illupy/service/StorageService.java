package org.illupy.service;

import org.illupy.dto.PresignResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * Abstraction for file storage.
 * Switch implementations by changing storage.type in application.properties.
 * "local"    → LocalStorageService   (local filesystem)
 * "supabase" → SupabaseStorageService (Supabase Storage — current)
 */
public interface StorageService {

    /**
     * Store a file server-side and return its public URL.
     */
    String store(MultipartFile file, String subDir);

    /**
     * Delete a file by its public URL.
     */
    void delete(String fileUrl);

    /**
     * Generate a presigned upload URL so the client can upload directly to storage.
     * Returns signedUrl (PUT target) and publicUrl (CDN URL to save in DB).
     * Default: throws UnsupportedOperationException (not all providers support this).
     */
    default PresignResponse presign(String subDir, String filename) {
        throw new UnsupportedOperationException("Presigned upload not supported by this storage provider");
    }
}
