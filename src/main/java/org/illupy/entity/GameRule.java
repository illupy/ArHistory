package org.illupy.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "game_rules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "scenario_id", nullable = false)
    private Long scenarioId;

    @Column(name = "token_code", nullable = false)
    private String tokenCode;

    @Column(name = "correct_zone_code", nullable = false)
    private String correctZoneCode;
}