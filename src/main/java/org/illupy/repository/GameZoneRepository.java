package org.illupy.repository;

import org.illupy.entity.GameZone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameZoneRepository extends JpaRepository<GameZone, Long> {
    List<GameZone> findByScenarioIdOrderByOrderIndexAsc(Long scenarioId);
}