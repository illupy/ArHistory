package org.illupy.service;

import org.illupy.dto.CreateMarkerRequest;
import org.illupy.dto.MarkerResponse;
import org.illupy.dto.UpdateMarkerRequest;

import java.util.List;

public interface MarkerService {
    Long create(CreateMarkerRequest request);
    List<MarkerResponse> getByLessonId(Long lessonId);
    MarkerResponse update(Long id, UpdateMarkerRequest request);
    void delete(Long id);
    MarkerResponse toggleActive(Long id);
}
