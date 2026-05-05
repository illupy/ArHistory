package org.illupy.repository;

import org.illupy.entity.Marker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MarkerRepository extends JpaRepository<Marker, Long> {
    Optional<Marker> findByMarkerCode(String markerCode);
    List<Marker> findByLessonId(Long lessonId);
}