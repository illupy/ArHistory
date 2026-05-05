package org.illupy.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateAssetRequest {
    @NotNull
    private Long lessonId;

    @NotBlank
    private String type; // TEXT, IMAGE, AUDIO, MODEL_3D

    private String fileUrl;
    private String content;
    private Integer orderIndex;
}
