package org.illupy.repository;

import org.illupy.entity.GameRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameRuleRepository extends JpaRepository<GameRule, Long> {
    List<GameRule> findByScenarioId(Long scenarioId);
}