package org.illupy.repository;

import org.illupy.entity.MarkerModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MarkerModelRepository extends JpaRepository<MarkerModel, Long> {
    Optional<MarkerModel> findByMarkerCode(String markerCode);
    boolean existsByMarkerCode(String markerCode);
}
