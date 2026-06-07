package org.illupy.service;

import org.illupy.dto.CreateAnnotationRequest;
import org.illupy.dto.StepAnnotationResponse;
import org.illupy.dto.UpdateAnnotationRequest;

import java.util.List;

public interface AnnotationService {
    List<StepAnnotationResponse> getByLessonId(Long lessonId);
    StepAnnotationResponse createForLesson(Long lessonId, CreateAnnotationRequest request);
    StepAnnotationResponse update(Long id, UpdateAnnotationRequest request);
    void delete(Long id);
}
