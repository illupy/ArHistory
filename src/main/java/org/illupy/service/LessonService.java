package org.illupy.service;

import org.illupy.dto.CreateLessonRequest;
import org.illupy.dto.LessonDetailResponse;
import org.illupy.dto.LessonResponse;

import java.util.List;

public interface LessonService {
    LessonResponse create(CreateLessonRequest request);
    List<LessonResponse> getAll();
    LessonDetailResponse getByMarkerCode(String markerCode);

}
