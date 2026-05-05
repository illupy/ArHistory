package org.illupy.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class CreateQuestionRequest {
    @NotBlank
    private String questionText;

    private String type; // defaults to MULTIPLE_CHOICE

    @NotEmpty
    private List<AnswerInput> answers;

    @Data
    public static class AnswerInput {
        @NotBlank
        private String answerText;
        private boolean correct;
    }
}
