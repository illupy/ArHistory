package org.illupy.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.illupy.common.ApiResponse;
import org.illupy.dto.CreateAnnotationRequest;
import org.illupy.dto.StepAnnotationResponse;
import org.illupy.dto.UpdateAnnotationRequest;
import org.illupy.service.AnnotationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AnnotationController {

    private final AnnotationService annotationService;

    @GetMapping("/api/lessons/{lessonId}/annotations")
    public ApiResponse<List<StepAnnotationResponse>> getByLessonId(@PathVariable Long lessonId) {
        return ApiResponse.success(annotationService.getByLessonId(lessonId));
    }

    @PostMapping("/api/lessons/{lessonId}/annotations")
    public ApiResponse<StepAnnotationResponse> createForLesson(@PathVariable Long lessonId,
                                                       @Valid @RequestBody CreateAnnotationRequest request) {
        return ApiResponse.success(annotationService.createForLesson(lessonId, request), "Tạo chú thích thành công");
    }

    @PutMapping("/api/annotations/{id}")
    public ApiResponse<StepAnnotationResponse> update(@PathVariable Long id,
                                                       @RequestBody UpdateAnnotationRequest request) {
        return ApiResponse.success(annotationService.update(id, request), "Cập nhật chú thích thành công");
    }

    @DeleteMapping("/api/annotations/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        annotationService.delete(id);
        return ApiResponse.success(null, "Xóa chú thích thành công");
    }
}
