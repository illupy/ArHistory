package org.illupy.service;

import org.illupy.dto.CreateMarkerRequest;

public interface MarkerService {
    Long create(CreateMarkerRequest request);
}
