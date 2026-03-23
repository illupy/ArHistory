package org.illupy.service.impl;

import lombok.RequiredArgsConstructor;
import org.illupy.dto.CreateLessonRequest;
import org.illupy.dto.LessonDetailResponse;
import org.illupy.dto.LessonResponse;
import org.illupy.entity.Asset;
import org.illupy.entity.Lesson;
import org.illupy.entity.Marker;
import org.illupy.entity.User;
import org.illupy.enums.LessonStatus;
import org.illupy.exception.ResourceNotFoundException;
import org.illupy.repository.AssetRepository;
import org.illupy.repository.LessonRepository;
import org.illupy.repository.MarkerRepository;
import org.illupy.repository.UserRepository;
import org.illupy.service.LessonService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class LessonServiceImpl implements LessonService {
    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;
    private final MarkerRepository markerRepository;
    private final AssetRepository assetRepository;

    @Override
    public LessonResponse create(CreateLessonRequest request) {
        User teacher = userRepository.findById(1L)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));

        Lesson lesson = Lesson.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .content(request.getContent())
                .thumbnailUrl(request.getThumbnailUrl())
                .status(LessonStatus.DRAFT)
                .createdBy(teacher)
                .createdAt(LocalDateTime.now())
                .build();

        lesson = lessonRepository.save(lesson);

        return toResponse(lesson);
    }

    @Override
    public List<LessonResponse> getAll() {
        return lessonRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public LessonDetailResponse getByMarkerCode(String markerCode) {
        Marker marker = markerRepository.findByMarkerCode(markerCode)
                .orElseThrow(() -> new ResourceNotFoundException("Marker not found"));

        Lesson lesson = marker.getLesson();
        List<Asset> assets = assetRepository.findByLessonIdOrderByOrderIndexAsc(lesson.getId());

        return LessonDetailResponse.builder()
                .id(lesson.getId())
                .title(lesson.getTitle())
                .description(lesson.getDescription())
                .content(lesson.getContent())
                .thumbnailUrl(lesson.getThumbnailUrl())
                .status(lesson.getStatus().name())
                .assets(assets.stream().map(asset ->
                        LessonDetailResponse.LessonAssetItem.builder()
                                .id(asset.getId())
                                .type(asset.getType().name())
                                .fileUrl(asset.getFileUrl())
                                .content(asset.getContent())
                                .orderIndex(asset.getOrderIndex())
                                .build()
                ).toList())
                .build();
    }

    private LessonResponse toResponse(Lesson lesson) {
        return LessonResponse.builder()
                .id(lesson.getId())
                .title(lesson.getTitle())
                .description(lesson.getDescription())
                .content(lesson.getContent())
                .thumbnailUrl(lesson.getThumbnailUrl())
                .status(lesson.getStatus().name())
                .build();
    }
}
