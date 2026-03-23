package org.illupy.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateLessonRequest {
    @NotBlank
    private String title;
    private String description;
    private String content;
    private String thumbnailUrl;
}