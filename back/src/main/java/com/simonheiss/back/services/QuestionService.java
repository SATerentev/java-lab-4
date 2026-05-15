package com.simonheiss.back.services;

import com.simonheiss.back.DTO.QuestionInfo;
import com.simonheiss.back.entity.Question;
import com.simonheiss.back.infrastructure.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {
    private final QuestionRepository repository;

    public QuestionService(QuestionRepository repository){
        this.repository = repository;
    }

    public int getNewQuestionId(int grade){
        Question newQuestion = repository.getRandomQuestion(grade);
        return newQuestion.getId();
    }

    public QuestionInfo getQuestionInfo(int id){
        Question question = repository.findById(id).get();
        List<String> answers = question.getAnswers();

        return new QuestionInfo(
                question.getText(),
                answers,
                question.getCorrectAnswer(),
                question.getGrade()
        );
    }

    public boolean checkAnswer(int questionId, int answer){
        return repository.findById(questionId).get().getCorrectAnswer() == answer;
    }
}
