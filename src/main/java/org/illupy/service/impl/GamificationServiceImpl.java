package org.illupy.service.impl;

import lombok.RequiredArgsConstructor;
import org.illupy.dto.GameRuleResponse;
import org.illupy.dto.GameScenarioResponse;
import org.illupy.dto.GameTokenResponse;
import org.illupy.dto.GameZoneResponse;
import org.illupy.entity.GameScenario;
import org.illupy.exception.ResourceNotFoundException;
import org.illupy.repository.GameRuleRepository;
import org.illupy.repository.GameScenarioRepository;
import org.illupy.repository.GameTokenRepository;
import org.illupy.repository.GameZoneRepository;
import org.illupy.service.GamificationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GamificationServiceImpl implements GamificationService {

    private final GameScenarioRepository gameScenarioRepository;
    private final GameTokenRepository gameTokenRepository;
    private final GameZoneRepository gameZoneRepository;
    private final GameRuleRepository gameRuleRepository;

    @Override
    public GameScenarioResponse getByLessonId(Long lessonId) {
        GameScenario scenario = gameScenarioRepository.findByLessonIdAndStatus(lessonId, "ACTIVE")
                .orElseThrow(() -> new ResourceNotFoundException("Game scenario not found"));

        List<GameTokenResponse> tokens = gameTokenRepository.findByScenarioIdOrderByOrderIndexAsc(scenario.getId())
                .stream()
                .map(token -> GameTokenResponse.builder()
                        .id(token.getId())
                        .tokenCode(token.getTokenCode())
                        .displayName(token.getDisplayName())
                        .iconUrl(token.getIconUrl())
                        .orderIndex(token.getOrderIndex())
                        .build())
                .toList();

        List<GameZoneResponse> zones = gameZoneRepository.findByScenarioIdOrderByOrderIndexAsc(scenario.getId())
                .stream()
                .map(zone -> GameZoneResponse.builder()
                        .id(zone.getId())
                        .zoneCode(zone.getZoneCode())
                        .displayName(zone.getDisplayName())
                        .posX(zone.getPosX())
                        .posY(zone.getPosY())
                        .width(zone.getWidth())
                        .height(zone.getHeight())
                        .orderIndex(zone.getOrderIndex())
                        .build())
                .toList();

        List<GameRuleResponse> rules = gameRuleRepository.findByScenarioId(scenario.getId())
                .stream()
                .map(rule -> GameRuleResponse.builder()
                        .id(rule.getId())
                        .tokenCode(rule.getTokenCode())
                        .correctZoneCode(rule.getCorrectZoneCode())
                        .build())
                .toList();

        return GameScenarioResponse.builder()
                .id(scenario.getId())
                .lessonId(scenario.getLessonId())
                .title(scenario.getTitle())
                .instruction(scenario.getInstruction())
                .templateType(scenario.getTemplateType())
                .backgroundImageUrl(scenario.getBackgroundImageUrl())
                .tokens(tokens)
                .zones(zones)
                .rules(rules)
                .build();
    }
}