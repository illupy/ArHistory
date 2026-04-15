package org.illupy.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class QuizDetailResponse {
    private Long id;
    private String title;
    private Long lessonId;
    private List<QuestionResponse> questions;
}