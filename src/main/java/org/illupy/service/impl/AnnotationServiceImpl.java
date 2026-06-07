package org.illupy.service.impl;

import lombok.RequiredArgsConstructor;
import org.illupy.dto.CreateAnnotationRequest;
import org.illupy.dto.StepAnnotationResponse;
import org.illupy.dto.UpdateAnnotationRequest;
import org.illupy.entity.Lesson;
import org.illupy.entity.LessonStepAnnotation;
import org.illupy.exception.ResourceNotFoundException;
import org.illupy.repository.LessonRepository;
import org.illupy.repository.LessonStepAnnotationRepository;
import org.illupy.service.AnnotationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnnotationServiceImpl implements AnnotationService {

    private final LessonStepAnnotationRepository annotationRepository;
    private final LessonRepository lessonRepository;

    @Override
    public List<StepAnnotationResponse> getByLessonId(Long lessonId) {
        return annotationRepository.findByLessonIdAndIsDeletedFalseOrderByOrderIndexAsc(lessonId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public StepAnnotationResponse createForLesson(Long lessonId, CreateAnnotationRequest request) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));

        LessonStepAnnotation annotation = LessonStepAnnotation.builder()
                .lesson(lesson)
                .keyword(request.getKeyword())
                .title(request.getTitle())
                .description(request.getDescription())
                .annotationType(request.getAnnotationType())
                .mediaUrl(request.getMediaUrl())
                .modelCode(request.getModelCode())
                .orderIndex(request.getOrderIndex() != null ? request.getOrderIndex() : 0)
                .isDeleted(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        annotation = annotationRepository.save(annotation);
        return toResponse(annotation);
    }

    @Override
    public StepAnnotationResponse update(Long id, UpdateAnnotationRequest request) {
        LessonStepAnnotation annotation = annotationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Annotation not found"));

        if (annotation.getIsDeleted()) {
            throw new ResourceNotFoundException("Annotation not found");
        }

        if (request.getKeyword() != null) annotation.setKeyword(request.getKeyword());
        if (request.getTitle() != null) annotation.setTitle(request.getTitle());
        if (request.getDescription() != null) annotation.setDescription(request.getDescription());
        if (request.getAnnotationType() != null) annotation.setAnnotationType(request.getAnnotationType());
        if (request.getMediaUrl() != null) annotation.setMediaUrl(request.getMediaUrl());
        if (request.getModelCode() != null) annotation.setModelCode(request.getModelCode());
        if (request.getOrderIndex() != null) annotation.setOrderIndex(request.getOrderIndex());
        annotation.setUpdatedAt(LocalDateTime.now());

        annotation = annotationRepository.save(annotation);
        return toResponse(annotation);
    }

    @Override
    public void delete(Long id) {
        LessonStepAnnotation annotation = annotationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Annotation not found"));

        annotation.setIsDeleted(true);
        annotation.setUpdatedAt(LocalDateTime.now());
        annotationRepository.save(annotation);
    }

    private StepAnnotationResponse toResponse(LessonStepAnnotation annotation) {
        return StepAnnotationResponse.builder()
                .id(annotation.getId())
                .keyword(annotation.getKeyword())
                .title(annotation.getTitle())
                .description(annotation.getDescription())
                .annotationType(annotation.getAnnotationType())
                .mediaUrl(annotation.getMediaUrl())
                .modelCode(annotation.getModelCode())
                .orderIndex(annotation.getOrderIndex())
                .build();
    }
}
