package org.illupy.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateAnnotationRequest {
    @NotBlank
    private String keyword;

    private String title;
    private String description;

    @NotBlank
    private String annotationType; // IMAGE, MODEL

    private String mediaUrl;
    private String modelCode;
    private Integer orderIndex;
}
