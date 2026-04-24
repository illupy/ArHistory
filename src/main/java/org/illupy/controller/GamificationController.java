package org.illupy.controller;

import lombok.RequiredArgsConstructor;
import org.illupy.common.ApiResponse;
import org.illupy.dto.GameScenarioResponse;
import org.illupy.service.GamificationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gamification")
@RequiredArgsConstructor
public class GamificationController {

    private final GamificationService gamificationService;

    @GetMapping("/lesson/{lessonId}")
    public ApiResponse<GameScenarioResponse> getByLessonId(@PathVariable Long lessonId) {
        GameScenarioResponse response = gamificationService.getByLessonId(lessonId);
        return ApiResponse.success(response);
    }
}