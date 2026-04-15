package org.illupy.service.impl;

import lombok.RequiredArgsConstructor;
import org.illupy.dto.AnswerResponse;
import org.illupy.dto.QuestionResponse;
import org.illupy.dto.QuizDetailResponse;
import org.illupy.entity.Answer;
import org.illupy.entity.Question;
import org.illupy.entity.Quiz;
import org.illupy.exception.ResourceNotFoundException;
import org.illupy.repository.AnswerRepository;
import org.illupy.repository.QuestionRepository;
import org.illupy.repository.QuizRepository;
import org.illupy.service.QuizService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    @Override
    public QuizDetailResponse getByLessonId(Long lessonId) {
        Quiz quiz = quizRepository.findByLessonId(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found"));

        List<Question> questions = questionRepository.findByQuizIdOrderByIdAsc(quiz.getId());

        List<QuestionResponse> questionResponses = questions.stream().map(question -> {
            List<Answer> answers = answerRepository.findByQuestionIdOrderByIdAsc(question.getId());

            List<AnswerResponse> answerResponses = answers.stream()
                    .map(answer -> AnswerResponse.builder()
                            .id(answer.getId())
                            .content(answer.getAnswerText())
                            .correct(answer.getIsCorrect())
                            .build())
                    .toList();

            return QuestionResponse.builder()
                    .id(question.getId())
                    .question(question.getQuestionText())
                    .answers(answerResponses)
                    .build();
        }).toList();

        return QuizDetailResponse.builder()
                .id(quiz.getId())
                .title(quiz.getTitle())
                .lessonId(lessonId)
                .questions(questionResponses)
                .build();
    }
}