package org.illupy.repository;

import org.illupy.entity.LessonStepAnnotation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonStepAnnotationRepository extends JpaRepository<LessonStepAnnotation, Long> {
    List<LessonStepAnnotation> findByLessonIdAndIsDeletedFalseOrderByOrderIndexAsc(Long lessonId);
}
