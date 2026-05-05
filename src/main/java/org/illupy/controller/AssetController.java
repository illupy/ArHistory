package org.illupy.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.illupy.common.ApiResponse;
import org.illupy.dto.CreateAssetRequest;
import org.illupy.dto.LessonAssetResponse;
import org.illupy.dto.UpdateAssetRequest;
import org.illupy.service.AssetService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor
public class AssetController {

    private final AssetService assetService;

    @GetMapping("/lesson/{lessonId}")
    public ApiResponse<List<LessonAssetResponse>> getByLesson(@PathVariable Long lessonId) {
        return ApiResponse.success(assetService.getByLessonId(lessonId));
    }

    @PostMapping
    public ApiResponse<LessonAssetResponse> create(@Valid @RequestBody CreateAssetRequest request) {
        return ApiResponse.success(assetService.create(request), "Step created");
    }

    @PutMapping("/{id}")
    public ApiResponse<LessonAssetResponse> update(@PathVariable Long id, @RequestBody UpdateAssetRequest request) {
        return ApiResponse.success(assetService.update(id, request), "Step updated");
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        assetService.delete(id);
        return ApiResponse.success(null, "Step deleted");
    }
}
