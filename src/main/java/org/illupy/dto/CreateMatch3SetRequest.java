package org.illupy.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateMatch3SetRequest {
    @NotBlank
    private String imageUrl1;
    @NotBlank
    private String imageUrl2;
    @NotBlank
    private String imageUrl3;
    @NotBlank
    private String note;

    private String noteType; // TEXT, IMAGE, MODEL
    private String noteMediaUrl;
    private String noteModelCode;
}
