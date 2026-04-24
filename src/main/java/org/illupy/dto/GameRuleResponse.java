package org.illupy.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GameRuleResponse {
    private Long id;
    private String tokenCode;
    private String correctZoneCode;
}