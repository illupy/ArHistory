package org.illupy.service;

import org.illupy.dto.CreateAssetRequest;
import org.illupy.dto.LessonAssetResponse;
import org.illupy.dto.UpdateAssetRequest;

import java.util.List;

public interface AssetService {
    List<LessonAssetResponse> getByLessonId(Long lessonId);
    LessonAssetResponse create(CreateAssetRequest request);
    LessonAssetResponse update(Long id, UpdateAssetRequest request);
    void delete(Long id);
}
