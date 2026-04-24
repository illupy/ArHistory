package org.illupy.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GameZoneResponse {
    private Long id;
    private String zoneCode;
    private String displayName;
    private Float posX;
    private Float posY;
    private Float width;
    private Float height;
    private Integer orderIndex;
}