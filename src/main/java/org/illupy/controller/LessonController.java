package org.illupy.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.illupy.common.ApiResponse;
import org.illupy.dto.CreateLessonRequest;
import org.illupy.dto.LessonDetailResponse;
import org.illupy.dto.LessonResponse;
import org.illupy.service.LessonService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;

    @PostMapping
    public ApiResponse<LessonResponse> create(@Valid @RequestBody CreateLessonRequest request) {
        return ApiResponse.success(lessonService.create(request), "Lesson created successfully");
    }

    @GetMapping
    public ApiResponse<List<LessonResponse>> getAll() {
        return ApiResponse.success(lessonService.getAll());
    }

    @GetMapping("/by-marker/{markerCode}")
    public ApiResponse<LessonDetailResponse> getByMarkerCode(@PathVariable String markerCode) {
        return ApiResponse.success(lessonService.getByMarkerCode(markerCode));
    }
}
