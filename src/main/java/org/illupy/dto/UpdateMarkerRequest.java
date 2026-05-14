package org.illupy.dto;

import lombok.Data;

@Data
public class UpdateMarkerRequest {
    private String markerCode;
    private String imageUrl;
    private String previewModelCode;
    private String previewAudioUrl;
    private Boolean active;
}
