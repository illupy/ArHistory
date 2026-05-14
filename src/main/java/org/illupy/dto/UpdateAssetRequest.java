package org.illupy.dto;

import lombok.Data;

import java.util.List;

@Data
public class UpdateAssetRequest {
    private String type;
    private String fileUrl;
    private String content;
    private Integer orderIndex;
    private List<String> mediaUrls;
}
