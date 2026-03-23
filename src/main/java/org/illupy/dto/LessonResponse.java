package org.illupy.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LessonResponse {
    private Long id;
    private String title;
    private String description;
    private String content;
    private String thumbnailUrl;
    private String status;
}
