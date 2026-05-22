package org.illupy.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "marker_models")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarkerModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "marker_code", nullable = false, unique = true, length = 255)
    private String markerCode;

    @Column(name = "model_url", nullable = false, columnDefinition = "TEXT")
    private String modelUrl;

    @Column(name = "image_url", nullable = false, columnDefinition = "TEXT")
    private String imageUrl;

    @Column(name = "preview_model_code", columnDefinition = "TEXT")
    private String previewModelCode;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by", nullable = false, length = 255)
    private String createdBy;
}
