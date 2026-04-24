package org.illupy.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GameScenarioResponse {
    private Long id;
    private Long lessonId;
    private String title;
    private String instruction;
    private String templateType;
    private String backgroundImageUrl;
    private List<GameTokenResponse> tokens;
    private List<GameZoneResponse> zones;
    private List<GameRuleResponse> rules;
}