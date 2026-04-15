package org.illupy.service;

import org.illupy.dto.QuizDetailResponse;

public interface QuizService {
    QuizDetailResponse getByLessonId(Long lessonId);
}