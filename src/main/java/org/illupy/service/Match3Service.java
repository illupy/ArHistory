package org.illupy.service;

import org.illupy.dto.*;

import java.util.List;

public interface Match3Service {
    Match3SetResponse create(CreateMatch3SetRequest request);
    List<Match3SetResponse> getAll();
    Match3SetResponse getById(Long id);
    Match3SetResponse update(Long id, UpdateMatch3SetRequest request);
    void delete(Long id);
    Match3GameResponse getRandomGame();
}
