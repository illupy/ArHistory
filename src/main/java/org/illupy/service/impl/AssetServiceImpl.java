package org.illupy.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.illupy.dto.CreateAssetRequest;
import org.illupy.dto.LessonAssetResponse;
import org.illupy.dto.UpdateAssetRequest;
import org.illupy.entity.Asset;
import org.illupy.entity.Lesson;
import org.illupy.enums.AssetType;
import org.illupy.exception.ResourceNotFoundException;
import org.illupy.repository.AssetRepository;
import org.illupy.repository.LessonRepository;
import org.illupy.service.AssetService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AssetServiceImpl implements AssetService {

    private final AssetRepository assetRepository;
    private final LessonRepository lessonRepository;
    private final ObjectMapper objectMapper;

    @Override
    public List<LessonAssetResponse> getByLessonId(Long lessonId) {
        return assetRepository.findByLessonIdOrderByOrderIndexAsc(lessonId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public LessonAssetResponse create(CreateAssetRequest request) {
        Lesson lesson = lessonRepository.findById(request.getLessonId())
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));

        AssetType type = AssetType.valueOf(request.getType().toUpperCase());

        // Validate IMAGE_GALLERY max 4 images
        if (type == AssetType.IMAGE_GALLERY && request.getMediaUrls() != null && request.getMediaUrls().size() > 4) {
            throw new IllegalArgumentException("IMAGE_GALLERY chỉ chứa tối đa 4 ảnh");
        }

        Asset asset = Asset.builder()
                .lesson(lesson)
                .type(type)
                .fileUrl(request.getFileUrl())
                .content(request.getContent())
                .orderIndex(request.getOrderIndex())
                .mediaUrls(toJson(request.getMediaUrls()))
                .createdAt(LocalDateTime.now())
                .build();

        asset = assetRepository.save(asset);
        return toResponse(asset);
    }

    @Override
    public LessonAssetResponse update(Long id, UpdateAssetRequest request) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found"));

        if (request.getType() != null) asset.setType(AssetType.valueOf(request.getType().toUpperCase()));
        if (request.getFileUrl() != null) asset.setFileUrl(request.getFileUrl());
        if (request.getContent() != null) asset.setContent(request.getContent());
        if (request.getOrderIndex() != null) asset.setOrderIndex(request.getOrderIndex());
        if (request.getMediaUrls() != null) {
            if (request.getMediaUrls().size() > 4) {
                throw new IllegalArgumentException("IMAGE_GALLERY chỉ chứa tối đa 4 ảnh");
            }
            asset.setMediaUrls(toJson(request.getMediaUrls()));
        }

        asset = assetRepository.save(asset);
        return toResponse(asset);
    }

    @Override
    public void delete(Long id) {
        if (!assetRepository.existsById(id)) {
            throw new ResourceNotFoundException("Asset not found");
        }
        assetRepository.deleteById(id);
    }

    private LessonAssetResponse toResponse(Asset asset) {
        return LessonAssetResponse.builder()
                .id(asset.getId())
                .type(asset.getType() != null ? asset.getType().name() : null)
                .fileUrl(asset.getFileUrl())
                .content(asset.getContent())
                .orderIndex(asset.getOrderIndex())
                .mediaUrls(fromJson(asset.getMediaUrls()))
                .build();
    }

    private String toJson(List<String> urls) {
        if (urls == null || urls.isEmpty()) return null;
        try {
            return objectMapper.writeValueAsString(urls);
        } catch (JsonProcessingException e) {
            log.warn("Failed to serialize mediaUrls", e);
            return null;
        }
    }

    private List<String> fromJson(String json) {
        if (json == null || json.isBlank()) return Collections.emptyList();
        try {
            return objectMapper.readValue(json, new TypeReference<List<String>>() {});
        } catch (JsonProcessingException e) {
            log.warn("Failed to deserialize mediaUrls: {}", json, e);
            return Collections.emptyList();
        }
    }
}
