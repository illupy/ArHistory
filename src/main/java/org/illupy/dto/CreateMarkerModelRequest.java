package org.illupy.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateMarkerModelRequest {
    @NotBlank
    private String markerCode;

    @NotBlank
    private String modelUrl;

    @NotBlank
    private String imageUrl;

    private String previewModelCode;
}
