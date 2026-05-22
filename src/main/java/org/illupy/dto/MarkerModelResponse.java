package org.illupy.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MarkerModelResponse {
    private Long id;
    private String markerCode;
    private String modelUrl;
    private String imageUrl;
    private String previewModelCode;
    private String createdAt;
    private String createdBy;
}
