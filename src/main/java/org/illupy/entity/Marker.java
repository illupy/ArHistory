package org.illupy.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "markers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Marker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @Column(name = "image_url", nullable = false, columnDefinition = "TEXT")
    private String imageUrl;

    @Column(name = "marker_code", nullable = false, unique = true, length = 255)
    private String markerCode;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "preview_model_code")
    private String previewModelCode;

    @Column(name = "preview_audio_url")
    private String previewAudioUrl;

    @Column(name = "active", nullable = false)
    @Builder.Default
    private Boolean active = true;
}