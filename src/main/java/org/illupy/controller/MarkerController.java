package org.illupy.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.illupy.common.ApiResponse;
import org.illupy.dto.CreateMarkerRequest;
import org.illupy.dto.MarkerResponse;
import org.illupy.dto.UpdateMarkerRequest;
import org.illupy.service.MarkerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/markers")
@RequiredArgsConstructor
public class MarkerController {

    private final MarkerService markerService;

    @PostMapping
    public ApiResponse<Long> create(@Valid @RequestBody CreateMarkerRequest request) {
        return ApiResponse.success(markerService.create(request), "Marker created successfully");
    }

    @GetMapping("/lesson/{lessonId}")
    public ApiResponse<List<MarkerResponse>> getByLesson(@PathVariable Long lessonId) {
        return ApiResponse.success(markerService.getByLessonId(lessonId));
    }

    @PutMapping("/{id}")
    public ApiResponse<MarkerResponse> update(@PathVariable Long id, @RequestBody UpdateMarkerRequest request) {
        return ApiResponse.success(markerService.update(id, request), "Cập nhật marker thành công");
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        markerService.delete(id);
        return ApiResponse.success(null, "Xóa marker thành công");
    }

    @PutMapping("/{id}/toggle-active")
    public ApiResponse<MarkerResponse> toggleActive(@PathVariable Long id) {
        return ApiResponse.success(markerService.toggleActive(id));
    }
}