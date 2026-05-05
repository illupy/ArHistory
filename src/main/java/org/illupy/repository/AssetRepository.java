package org.illupy.repository;


import org.illupy.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssetRepository extends JpaRepository<Asset, Long> {
    List<Asset> findByLessonIdOrderByOrderIndexAsc(Long lessonId);
    void deleteAllByLessonId(Long lessonId);
    long countByLessonId(Long lessonId);
}