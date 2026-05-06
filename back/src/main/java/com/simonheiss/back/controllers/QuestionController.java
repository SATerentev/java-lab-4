package com.simonheiss.back.controllers;

import com.simonheiss.back.DTO.QuestionResponse;
import com.simonheiss.back.usecase.QuestionUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/api/question")
public class QuestionController {
    private final QuestionUseCase questionUseCase;

    @GetMapping("/get/{questionId}")
    public ResponseEntity<QuestionResponse> getQuestion(@PathVariable String questionId){
        QuestionResponse response = questionUseCase.getQuestionInfo(Integer.parseInt(questionId));

        return ResponseEntity.ok(response);
    }
}
