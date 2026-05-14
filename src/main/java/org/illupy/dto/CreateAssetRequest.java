package org.illupy.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CreateAssetRequest {
    @NotNull
    private Long lessonId;

    @NotBlank
    private String type; // TEXT, VIDEO, IMAGE_GALLERY

    private String fileUrl;
    private String content;
    private Integer orderIndex;
    private List<String> mediaUrls; // For IMAGE_GALLERY (max 4)
}
