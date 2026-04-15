package org.illupy.controller;

import lombok.RequiredArgsConstructor;
import org.illupy.common.ApiResponse;
import org.illupy.dto.QuizDetailResponse;
import org.illupy.service.QuizService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quizzes")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    @GetMapping("/lesson/{lessonId}")
    public ApiResponse<QuizDetailResponse> getByLessonId(@PathVariable Long lessonId) {
        QuizDetailResponse response = quizService.getByLessonId(lessonId);
        return ApiResponse.success(response);
    }
}