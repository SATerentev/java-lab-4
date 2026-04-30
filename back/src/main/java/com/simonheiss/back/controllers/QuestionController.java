package com.simonheiss.back.controllers;

import com.simonheiss.back.entity.Question;
import com.simonheiss.back.services.QuestionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {
    private final QuestionService questionService;

    public QuestionController(QuestionService service){
        questionService = service;
    }

    @GetMapping("/random")
    public Question GetRandomQuestion(){
        return questionService.getRandom();
    }
}
