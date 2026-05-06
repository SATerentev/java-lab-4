package com.simonheiss.back.services;

import com.simonheiss.back.entity.Question;
import com.simonheiss.back.infrastructure.QuestionRepository;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {
    private final QuestionRepository repository;

    public QuestionService(QuestionRepository repository){
        this.repository = repository;
    }

    public Question getNewQuestion(int grade){
        return repository.getRandomQuestion(grade);
    }

    public Question getQuestion(int id){
        return repository.findById(id).get();
    }

    public boolean checkAnswer(int questionId, int answer){
        return repository.findById(questionId).get().getCorrectAnswer() == answer;
    }
}
