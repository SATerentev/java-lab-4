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

    public Question getRandom(){
        return repository.getRandomQuestion();
    }
}
