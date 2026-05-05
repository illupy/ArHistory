package org.illupy.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.illupy.common.ApiResponse;
import org.illupy.dto.CreateMarkerRequest;
import org.illupy.dto.MarkerResponse;
import org.illupy.entity.Marker;
import org.illupy.repository.MarkerRepository;
import org.illupy.service.MarkerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/markers")
@RequiredArgsConstructor
public class MarkerController {

    private final MarkerService markerService;
    private final MarkerRepository markerRepository;

    @PostMapping
    public ApiResponse<Long> create(@Valid @RequestBody CreateMarkerRequest request) {
        return ApiResponse.success(markerService.create(request), "Marker created successfully");
    }

    @GetMapping("/lesson/{lessonId}")
    public ApiResponse<List<MarkerResponse>> getByLesson(@PathVariable Long lessonId) {
        List<MarkerResponse> markers = markerRepository.findByLessonId(lessonId).stream()
                .map(m -> MarkerResponse.builder()
                        .id(m.getId())
                        .lessonId(lessonId)
                        .markerCode(m.getMarkerCode())
                        .imageUrl(m.getImageUrl())
                        .createdAt(m.getCreatedAt() != null ? m.getCreatedAt().toString() : null)
                        .build())
                .toList();
        return ApiResponse.success(markers);
    }
}