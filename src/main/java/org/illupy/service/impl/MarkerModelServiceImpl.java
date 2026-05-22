package org.illupy.service.impl;

import lombok.RequiredArgsConstructor;
import org.illupy.dto.CreateMarkerModelRequest;
import org.illupy.dto.MarkerModelResponse;
import org.illupy.dto.UpdateMarkerModelRequest;
import org.illupy.entity.MarkerModel;
import org.illupy.exception.ResourceNotFoundException;
import org.illupy.repository.MarkerModelRepository;
import org.illupy.service.MarkerModelService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MarkerModelServiceImpl implements MarkerModelService {

    private final MarkerModelRepository markerModelRepository;

    @Override
    public MarkerModelResponse create(CreateMarkerModelRequest request, String createdBy) {
        if (markerModelRepository.existsByMarkerCode(request.getMarkerCode())) {
            throw new IllegalArgumentException("Marker code đã tồn tại: " + request.getMarkerCode());
        }

        MarkerModel entity = MarkerModel.builder()
                .markerCode(request.getMarkerCode())
                .modelUrl(request.getModelUrl())
                .imageUrl(request.getImageUrl())
                .previewModelCode(request.getPreviewModelCode())
                .createdAt(LocalDateTime.now())
                .createdBy(createdBy)
                .build();

        entity = markerModelRepository.save(entity);
        return toResponse(entity);
    }

    @Override
    public List<MarkerModelResponse> getAll() {
        return markerModelRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public MarkerModelResponse getById(Long id) {
        MarkerModel entity = markerModelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MarkerModel not found"));
        return toResponse(entity);
    }

    @Override
    public MarkerModelResponse update(Long id, UpdateMarkerModelRequest request) {
        MarkerModel entity = markerModelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MarkerModel not found"));

        if (request.getMarkerCode() != null) {
            // Check uniqueness if code is being changed
            if (!entity.getMarkerCode().equals(request.getMarkerCode())
                    && markerModelRepository.existsByMarkerCode(request.getMarkerCode())) {
                throw new IllegalArgumentException("Marker code đã tồn tại: " + request.getMarkerCode());
            }
            entity.setMarkerCode(request.getMarkerCode());
        }
        if (request.getModelUrl() != null) entity.setModelUrl(request.getModelUrl());
        if (request.getImageUrl() != null) entity.setImageUrl(request.getImageUrl());
        if (request.getPreviewModelCode() != null) entity.setPreviewModelCode(request.getPreviewModelCode());

        entity = markerModelRepository.save(entity);
        return toResponse(entity);
    }

    @Override
    public void delete(Long id) {
        if (!markerModelRepository.existsById(id)) {
            throw new ResourceNotFoundException("MarkerModel not found");
        }
        markerModelRepository.deleteById(id);
    }

    private MarkerModelResponse toResponse(MarkerModel m) {
        return MarkerModelResponse.builder()
                .id(m.getId())
                .markerCode(m.getMarkerCode())
                .modelUrl(m.getModelUrl())
                .imageUrl(m.getImageUrl())
                .previewModelCode(m.getPreviewModelCode())
                .createdAt(m.getCreatedAt() != null ? m.getCreatedAt().toString() : null)
                .createdBy(m.getCreatedBy())
                .build();
    }
}
