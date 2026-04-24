package org.illupy.repository;

import org.illupy.entity.GameToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameTokenRepository extends JpaRepository<GameToken, Long> {
    List<GameToken> findByScenarioIdOrderByOrderIndexAsc(Long scenarioId);
}