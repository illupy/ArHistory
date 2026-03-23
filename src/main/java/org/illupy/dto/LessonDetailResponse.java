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
    private List<LessonAssetItem> assets;

    @Data
    @Builder
    public static class LessonAssetItem {
        private Long id;
        private String type;
        private String fileUrl;
        private String content;
        private Integer orderIndex;
    }
}