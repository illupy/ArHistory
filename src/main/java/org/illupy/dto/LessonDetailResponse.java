package org.illupy.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class LessonDetailResponse {
    private Long id;
    private String title;
    private String description;
    private String content;
    private String thumbnailUrl;
    private String status;
    private String previewAudioUrl;
    private String previewModelCode;
    private Boolean hasQuiz;
    private Boolean hasGamification;
    private List<LessonAssetResponse> assets;
}