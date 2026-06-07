package org.illupy.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StepAnnotationResponse {
    private Long id;
    private String keyword;
    private String title;
    private String description;
    private String annotationType;
    private String mediaUrl;
    private String modelCode;
    private Integer orderIndex;
}
