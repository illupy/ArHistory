package org.illupy.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "game_zones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameZone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "scenario_id", nullable = false)
    private Long scenarioId;

    @Column(name = "zone_code", nullable = false)
    private String zoneCode;

    @Column(name = "display_name", nullable = false)
    private String displayName;

    @Column(name = "pos_x", nullable = false)
    private Float posX;

    @Column(name = "pos_y", nullable = false)
    private Float posY;

    private Float width;
    private Float height;

    @Column(name = "order_index")
    private Integer orderIndex;
}