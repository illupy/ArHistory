package org.illupy.repository;

import org.illupy.entity.GameScenario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GameScenarioRepository extends JpaRepository<GameScenario, Long> {
    Optional<GameScenario> findByLessonIdAndStatus(Long lessonId, String status);
}