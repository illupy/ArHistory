package org.illupy.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GameTokenResponse {
    private Long id;
    private String tokenCode;
    private String displayName;
    private String iconUrl;
    private Integer orderIndex;
}