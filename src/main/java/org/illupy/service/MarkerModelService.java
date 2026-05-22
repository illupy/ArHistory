package org.illupy.service;

import org.illupy.dto.CreateMarkerModelRequest;
import org.illupy.dto.MarkerModelResponse;
import org.illupy.dto.UpdateMarkerModelRequest;

import java.util.List;

public interface MarkerModelService {
    MarkerModelResponse create(CreateMarkerModelRequest request, String createdBy);
    List<MarkerModelResponse> getAll();
    MarkerModelResponse getById(Long id);
    MarkerModelResponse update(Long id, UpdateMarkerModelRequest request);
    void delete(Long id);
}
