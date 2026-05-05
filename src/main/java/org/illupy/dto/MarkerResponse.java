package org.illupy.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MarkerResponse {
    private Long id;
    private Long lessonId;
    private String markerCode;
    private String imageUrl;
    private String createdAt;
}
