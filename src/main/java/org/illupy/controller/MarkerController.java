package org.illupy.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.illupy.common.ApiResponse;
import org.illupy.dto.CreateMarkerRequest;
import org.illupy.service.MarkerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/markers")
@RequiredArgsConstructor
public class MarkerController {

    private final MarkerService markerService;

    @PostMapping
    public ApiResponse<Long> create(@Valid @RequestBody CreateMarkerRequest request) {
        return ApiResponse.success(markerService.create(request), "Marker created successfully");
    }
}