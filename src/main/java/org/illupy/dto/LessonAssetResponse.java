package org.illupy.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LessonAssetResponse {
    private Long id;
    private String type;
    private String fileUrl;
    private String content;
    private Integer orderIndex;
}