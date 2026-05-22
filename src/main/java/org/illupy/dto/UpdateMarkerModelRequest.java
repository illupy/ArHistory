package org.illupy.dto;

import lombok.Data;

@Data
public class UpdateMarkerModelRequest {
    private String markerCode;
    private String modelUrl;
    private String imageUrl;
    private String previewModelCode;
}
