package org.illupy.dto;

import lombok.Data;

@Data
public class UpdateLessonRequest {
    private String title;
    private String description;
    private String content;
    private String thumbnailUrl;
}
