package org.illupy.dto;

import lombok.Data;

@Data
public class UpdateAnnotationRequest {
    private String keyword;
    private String title;
    private String description;
    private String annotationType;
    private String mediaUrl;
    private String modelCode;
    private Integer orderIndex;
}
