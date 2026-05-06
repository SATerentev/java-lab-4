package com.simonheiss.back.usecase;

import com.simonheiss.back.DTO.QuestionResponse;
import com.simonheiss.back.entity.Question;
import com.simonheiss.back.services.QuestionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class QuestionUseCase {
    private final QuestionService questionService;

    public QuestionResponse getQuestionInfo(int questionId){
        Question question = questionService.getQuestion(questionId);

        return new QuestionResponse(
                question.getText(),
                question.getAnswers().get(0),
                question.getAnswers().get(1),
                question.getAnswers().get(2),
                question.getAnswers().get(3)
        );
    }
}
