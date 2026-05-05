package org.illupy.controller;

import lombok.RequiredArgsConstructor;
import org.illupy.common.ApiResponse;
import org.illupy.dto.DashboardStatsResponse;
import org.illupy.service.LessonService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final LessonService lessonService;

    @GetMapping("/stats")
    public ApiResponse<DashboardStatsResponse> getStats() {
        return ApiResponse.success(lessonService.getStats());
    }
}
