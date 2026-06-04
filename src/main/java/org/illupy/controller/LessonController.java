package org.illupy.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.illupy.common.ApiResponse;
import org.illupy.dto.*;
import org.illupy.service.LessonService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;

    @PostMapping
    public ApiResponse<LessonResponse> create(@Valid @RequestBody CreateLessonRequest request,
                                               Authentication authentication) {
        String createdBy = authentication.getName();
        return ApiResponse.success(lessonService.create(request, createdBy), "Lesson created successfully");
    }

    @GetMapping
    public ApiResponse<List<LessonResponse>> getAll() {
        return ApiResponse.success(lessonService.getAll());
    }

    @GetMapping("/{id}")
    public ApiResponse<LessonDetailResponse> getById(@PathVariable Long id) {
        return ApiResponse.success(lessonService.getById(id));
    }

    @PutMapping("/{id}")
    public ApiResponse<LessonResponse> update(@PathVariable Long id, @RequestBody UpdateLessonRequest request) {
        return ApiResponse.success(lessonService.update(id, request), "Cập nhật thành công");
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        lessonService.delete(id);
        return ApiResponse.success(null, "Xóa bài học thành công");
    }

    @PutMapping("/{id}/status")
    public ApiResponse<LessonResponse> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String status = body.get("status");
        return ApiResponse.success(lessonService.updateStatus(id, status));
    }

    @GetMapping("/by-marker/{markerCode}")
    public ApiResponse<LessonDetailResponse> getByMarkerCode(@PathVariable String markerCode) {
        return ApiResponse.success(lessonService.getByMarkerCode(markerCode));
    }
}
