package org.illupy.dto;

import lombok.Data;

@Data
public class UpdateAssetRequest {
    private String type;
    private String fileUrl;
    private String content;
    private Integer orderIndex;
}
