package org.illupy.service;

import org.illupy.dto.*;

import java.util.List;

public interface LessonService {
    LessonResponse create(CreateLessonRequest request);
    List<LessonResponse> getAll();
    LessonDetailResponse getById(Long id);
    LessonDetailResponse getByMarkerCode(String markerCode);
    LessonResponse update(Long id, UpdateLessonRequest request);
    void delete(Long id);
    LessonResponse updateStatus(Long id, String status);
    DashboardStatsResponse getStats();
}
