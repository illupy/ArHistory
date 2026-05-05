package org.illupy.service.impl;

import lombok.RequiredArgsConstructor;
import org.illupy.dto.*;
import org.illupy.entity.Asset;
import org.illupy.entity.Lesson;
import org.illupy.entity.Marker;
import org.illupy.entity.User;
import org.illupy.enums.LessonStatus;
import org.illupy.exception.ResourceNotFoundException;
import org.illupy.repository.*;
import org.illupy.service.LessonService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class LessonServiceImpl implements LessonService {
    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;
    private final MarkerRepository markerRepository;
    private final AssetRepository assetRepository;
    private final QuizRepository quizRepository;
    private final GameScenarioRepository gameScenarioRepository;

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
                .map(this::toFullResponse)
                .toList();
    }

    @Override
    public LessonDetailResponse getById(Long id) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));
        return toDetailResponse(lesson);
    }

    @Override
    public LessonDetailResponse getByMarkerCode(String markerCode) {
        Marker marker = markerRepository.findByMarkerCode(markerCode)
                .orElseThrow(() -> new ResourceNotFoundException("Marker not found"));
        return toDetailResponse(marker.getLesson());
    }

    @Override
    public LessonResponse update(Long id, UpdateLessonRequest request) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));

        if (request.getTitle() != null) lesson.setTitle(request.getTitle());
        if (request.getDescription() != null) lesson.setDescription(request.getDescription());
        if (request.getContent() != null) lesson.setContent(request.getContent());
        if (request.getThumbnailUrl() != null) lesson.setThumbnailUrl(request.getThumbnailUrl());
        if (request.getPreviewAudioUrl() != null) lesson.setPreviewAudioUrl(request.getPreviewAudioUrl());
        if (request.getPreviewModelCode() != null) lesson.setPreviewModelCode(request.getPreviewModelCode());

        lesson = lessonRepository.save(lesson);
        return toResponse(lesson);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!lessonRepository.existsById(id)) {
            throw new ResourceNotFoundException("Lesson not found");
        }
        lessonRepository.deleteById(id);
    }

    @Override
    public LessonResponse updateStatus(Long id, String status) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));

        lesson.setStatus(LessonStatus.valueOf(status.toUpperCase()));
        lesson = lessonRepository.save(lesson);
        return toResponse(lesson);
    }

    @Override
    public DashboardStatsResponse getStats() {
        List<Lesson> all = lessonRepository.findAll();
        long total = all.size();
        long published = all.stream().filter(l -> l.getStatus() == LessonStatus.PUBLISH).count();
        long draft = all.stream().filter(l -> l.getStatus() == LessonStatus.DRAFT).count();
        long markers = markerRepository.count();
        long quizzes = quizRepository.count();
        long games = gameScenarioRepository.count();

        return DashboardStatsResponse.builder()
                .totalLessons(total)
                .publishedLessons(published)
                .draftLessons(draft)
                .totalMarkers(markers)
                .totalQuizzes(quizzes)
                .totalGames(games)
                .build();
    }

    private LessonDetailResponse toDetailResponse(Lesson lesson) {
        List<Asset> assets = assetRepository.findByLessonIdOrderByOrderIndexAsc(lesson.getId());
        boolean hasQuiz = quizRepository.findByLessonId(lesson.getId()).isPresent();
        boolean hasGamification = gameScenarioRepository.findByLessonIdAndStatus(lesson.getId(), "ACTIVE").isPresent();
        List<Marker> markers = markerRepository.findByLessonId(lesson.getId());

        List<LessonAssetResponse> assetResponses = assets.stream()
                .map(asset -> LessonAssetResponse.builder()
                        .id(asset.getId())
                        .type(asset.getType() != null ? asset.getType().name() : null)
                        .fileUrl(asset.getFileUrl())
                        .content(asset.getContent())
                        .orderIndex(asset.getOrderIndex())
                        .build())
                .toList();

        return LessonDetailResponse.builder()
                .id(lesson.getId())
                .title(lesson.getTitle())
                .description(lesson.getDescription())
                .content(lesson.getContent())
                .thumbnailUrl(lesson.getThumbnailUrl())
                .status(lesson.getStatus() != null ? lesson.getStatus().name() : null)
                .previewAudioUrl(lesson.getPreviewAudioUrl())
                .previewModelCode(lesson.getPreviewModelCode())
                .hasQuiz(hasQuiz)
                .hasGamification(hasGamification)
                .hasMarker(!markers.isEmpty())
                .stepCount(assets.size())
                .assets(assetResponses)
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

    private LessonResponse toFullResponse(Lesson lesson) {
        boolean hasQuiz = quizRepository.findByLessonId(lesson.getId()).isPresent();
        boolean hasGame = gameScenarioRepository.findByLessonIdAndStatus(lesson.getId(), "ACTIVE").isPresent();
        List<Marker> markers = markerRepository.findByLessonId(lesson.getId());

        return LessonResponse.builder()
                .id(lesson.getId())
                .title(lesson.getTitle())
                .description(lesson.getDescription())
                .content(lesson.getContent())
                .thumbnailUrl(lesson.getThumbnailUrl())
                .status(lesson.getStatus().name())
                .hasQuiz(hasQuiz)
                .hasGamification(hasGame)
                .markerCode(markers.isEmpty() ? null : markers.get(0).getMarkerCode())
                .build();
    }
}
