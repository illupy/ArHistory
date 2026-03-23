package org.illupy.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateMarkerRequest {
    @NotNull
    private Long lessonId;

    @NotBlank
    private String imageUrl;

    @NotBlank
    private String markerCode;
}