package org.illupy.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "game_scenarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameScenario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lesson_id", nullable = false)
    private Long lessonId;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String instruction;

    @Column(name = "template_type", nullable = false)
    private String templateType;

    @Column(name = "background_image_url")
    private String backgroundImageUrl;

    private String status;
}