package org.illupy.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardStatsResponse {
    private long totalLessons;
    private long publishedLessons;
    private long draftLessons;
    private long totalMarkers;
    private long totalQuizzes;
    private long totalGames;
}
