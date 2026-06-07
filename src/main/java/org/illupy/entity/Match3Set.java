package org.illupy.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "match3_sets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Match3Set {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_url_1", nullable = false, columnDefinition = "TEXT")
    private String imageUrl1;

    @Column(name = "image_url_2", nullable = false, columnDefinition = "TEXT")
    private String imageUrl2;

    @Column(name = "image_url_3", nullable = false, columnDefinition = "TEXT")
    private String imageUrl3;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String note;

    @Column(name = "note_type", length = 50)
    @Builder.Default
    private String noteType = "TEXT"; // TEXT, IMAGE, MODEL

    @Column(name = "note_media_url", columnDefinition = "TEXT")
    private String noteMediaUrl;

    @Column(name = "note_model_code", length = 255)
    private String noteModelCode;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
