package org.illupy.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class LessonAssetResponse {
    private Long id;
    private String type;
    private String fileUrl;
    private String content;
    private Integer orderIndex;
    private List<String> mediaUrls;
}