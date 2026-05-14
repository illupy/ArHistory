package org.illupy.service.impl;

import lombok.RequiredArgsConstructor;
import org.illupy.dto.CreateMarkerRequest;
import org.illupy.dto.MarkerResponse;
import org.illupy.dto.UpdateMarkerRequest;
import org.illupy.entity.Lesson;
import org.illupy.entity.Marker;
import org.illupy.exception.ResourceNotFoundException;
import org.illupy.repository.LessonRepository;
import org.illupy.repository.MarkerRepository;
import org.illupy.service.MarkerService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MarkerServiceImpl implements MarkerService {
    private final MarkerRepository markerRepository;
    private final LessonRepository lessonRepository;

    @Override
    public Long create(CreateMarkerRequest request) {
        Lesson lesson = lessonRepository.findById(request.getLessonId())
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));

        Marker marker = Marker.builder()
                .lesson(lesson)
                .imageUrl(request.getImageUrl())
                .markerCode(request.getMarkerCode())
                .previewModelCode(request.getPreviewModelCode())
                .previewAudioUrl(request.getPreviewAudioUrl())
                .active(request.getActive() != null ? request.getActive() : true)
                .createdAt(LocalDateTime.now())
                .build();

        return markerRepository.save(marker).getId();
    }

    @Override
    public List<MarkerResponse> getByLessonId(Long lessonId) {
        return markerRepository.findByLessonId(lessonId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public MarkerResponse update(Long id, UpdateMarkerRequest request) {
        Marker marker = markerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Marker not found"));

        if (request.getMarkerCode() != null) marker.setMarkerCode(request.getMarkerCode());
        if (request.getImageUrl() != null) marker.setImageUrl(request.getImageUrl());
        if (request.getPreviewModelCode() != null) marker.setPreviewModelCode(request.getPreviewModelCode());
        if (request.getPreviewAudioUrl() != null) marker.setPreviewAudioUrl(request.getPreviewAudioUrl());
        if (request.getActive() != null) marker.setActive(request.getActive());

        marker = markerRepository.save(marker);
        return toResponse(marker);
    }

    @Override
    public void delete(Long id) {
        if (!markerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Marker not found");
        }
        markerRepository.deleteById(id);
    }

    @Override
    public MarkerResponse toggleActive(Long id) {
        Marker marker = markerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Marker not found"));

        marker.setActive(!marker.getActive());
        marker = markerRepository.save(marker);
        return toResponse(marker);
    }

    private MarkerResponse toResponse(Marker m) {
        return MarkerResponse.builder()
                .id(m.getId())
                .lessonId(m.getLesson().getId())
                .markerCode(m.getMarkerCode())
                .imageUrl(m.getImageUrl())
                .previewModelCode(m.getPreviewModelCode())
                .previewAudioUrl(m.getPreviewAudioUrl())
                .active(m.getActive())
                .createdAt(m.getCreatedAt() != null ? m.getCreatedAt().toString() : null)
                .build();
    }
}
