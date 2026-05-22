package org.illupy.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.illupy.common.ApiResponse;
import org.illupy.dto.CreateMarkerModelRequest;
import org.illupy.dto.MarkerModelResponse;
import org.illupy.dto.UpdateMarkerModelRequest;
import org.illupy.service.MarkerModelService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/marker-models")
@RequiredArgsConstructor
public class MarkerModelController {

    private final MarkerModelService markerModelService;

    @GetMapping
    public ApiResponse<List<MarkerModelResponse>> getAll() {
        return ApiResponse.success(markerModelService.getAll());
    }

    @GetMapping("/{id}")
    public ApiResponse<MarkerModelResponse> getById(@PathVariable Long id) {
        return ApiResponse.success(markerModelService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<MarkerModelResponse> create(@Valid @RequestBody CreateMarkerModelRequest request,
                                                    Authentication authentication) {
        String createdBy = authentication.getName();
        return ApiResponse.success(markerModelService.create(request, createdBy), "Tạo marker-model thành công");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<MarkerModelResponse> update(@PathVariable Long id,
                                                    @RequestBody UpdateMarkerModelRequest request) {
        return ApiResponse.success(markerModelService.update(id, request), "Cập nhật thành công");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        markerModelService.delete(id);
        return ApiResponse.success(null, "Xóa thành công");
    }
}
