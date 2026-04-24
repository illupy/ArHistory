package org.illupy.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "game_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "scenario_id", nullable = false)
    private Long scenarioId;

    @Column(name = "token_code", nullable = false)
    private String tokenCode;

    @Column(name = "display_name", nullable = false)
    private String displayName;

    @Column(name = "icon_url")
    private String iconUrl;

    @Column(name = "order_index")
    private Integer orderIndex;
}