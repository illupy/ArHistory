package org.illupy.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.illupy.common.ApiResponse;
import org.illupy.dto.*;
import org.illupy.entity.Answer;
import org.illupy.entity.Lesson;
import org.illupy.entity.Question;
import org.illupy.entity.Quiz;
import org.illupy.exception.ResourceNotFoundException;
import org.illupy.repository.AnswerRepository;
import org.illupy.repository.LessonRepository;
import org.illupy.repository.QuestionRepository;
import org.illupy.repository.QuizRepository;
import org.illupy.service.QuizService;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/quizzes")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;
    private final QuizRepository quizRepository;
    private final LessonRepository lessonRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    @GetMapping("/lesson/{lessonId}")
    public ApiResponse<QuizDetailResponse> getByLessonId(@PathVariable Long lessonId) {
        QuizDetailResponse response = quizService.getByLessonId(lessonId);
        return ApiResponse.success(response);
    }

    @PostMapping
    public ApiResponse<QuizDetailResponse> create(@Valid @RequestBody CreateQuizRequest request) {
        Lesson lesson = lessonRepository.findById(request.getLessonId())
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));

        // Check if quiz already exists
        if (quizRepository.findByLessonId(request.getLessonId()).isPresent()) {
            throw new IllegalArgumentException("Quiz đã tồn tại cho bài học này");
        }

        Quiz quiz = new Quiz();
        quiz.setLesson(lesson);
        quiz.setTitle(request.getTitle());
        quiz.setCreatedAt(Instant.now());
        quiz = quizRepository.save(quiz);

        return ApiResponse.success(quizService.getByLessonId(request.getLessonId()), "Quiz created");
    }

    @PostMapping("/{quizId}/questions")
    public ApiResponse<QuizDetailResponse> addQuestion(@PathVariable Long quizId,
                                                        @Valid @RequestBody CreateQuestionRequest request) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found"));

        Question question = new Question();
        question.setQuiz(quiz);
        question.setQuestionText(request.getQuestionText());
        question.setType(request.getType() != null ? request.getType() : "MULTIPLE_CHOICE");
        question.setCreatedAt(Instant.now());
        question = questionRepository.save(question);

        for (CreateQuestionRequest.AnswerInput ai : request.getAnswers()) {
            Answer answer = new Answer();
            answer.setQuestion(question);
            answer.setAnswerText(ai.getAnswerText());
            answer.setIsCorrect(ai.isCorrect());
            answerRepository.save(answer);
        }

        return ApiResponse.success(quizService.getByLessonId(quiz.getLesson().getId()), "Question added");
    }

    @DeleteMapping("/questions/{questionId}")
    public ApiResponse<Void> deleteQuestion(@PathVariable Long questionId) {
        if (!questionRepository.existsById(questionId)) {
            throw new ResourceNotFoundException("Question not found");
        }
        questionRepository.deleteById(questionId);
        return ApiResponse.success(null, "Question deleted");
    }

    @DeleteMapping("/{quizId}")
    public ApiResponse<Void> deleteQuiz(@PathVariable Long quizId) {
        if (!quizRepository.existsById(quizId)) {
            throw new ResourceNotFoundException("Quiz not found");
        }
        quizRepository.deleteById(quizId);
        return ApiResponse.success(null, "Quiz deleted");
    }
}