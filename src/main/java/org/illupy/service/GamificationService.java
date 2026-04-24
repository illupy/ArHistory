package org.illupy.service;

import org.illupy.dto.GameScenarioResponse;

public interface GamificationService {
    GameScenarioResponse getByLessonId(Long lessonId);
}