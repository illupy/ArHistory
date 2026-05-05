package org.illupy.service.impl;

import lombok.RequiredArgsConstructor;
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
import java.util.List;

@Service
@RequiredArgsConstructor
public class AssetServiceImpl implements AssetService {

    private final AssetRepository assetRepository;
    private final LessonRepository lessonRepository;

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

        Asset asset = Asset.builder()
                .lesson(lesson)
                .type(AssetType.valueOf(request.getType().toUpperCase()))
                .fileUrl(request.getFileUrl())
                .content(request.getContent())
                .orderIndex(request.getOrderIndex())
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
                .build();
    }
}
