package org.illupy.service.impl;

import lombok.RequiredArgsConstructor;
import org.illupy.dto.CreateMarkerRequest;
import org.illupy.entity.Lesson;
import org.illupy.entity.Marker;
import org.illupy.exception.ResourceNotFoundException;
import org.illupy.repository.LessonRepository;
import org.illupy.repository.MarkerRepository;
import org.illupy.service.LessonService;
import org.illupy.service.MarkerService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MarkerServiceImpl implements MarkerService {
    private final MarkerRepository markerRepository;
    private final LessonRepository lessonRepository;

    public Long create(CreateMarkerRequest request) {
        Lesson lesson = lessonRepository.findById(request.getLessonId())
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));

        Marker marker = Marker.builder()
                .lesson(lesson)
                .imageUrl(request.getImageUrl())
                .markerCode(request.getMarkerCode())
                .createdAt(LocalDateTime.now())
                .build();

        return markerRepository.save(marker).getId();
    }
}
