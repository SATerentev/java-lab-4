package com.simonheiss.back.DTO.Hints;

import com.simonheiss.back.DTO.QuestionResponse;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ChangeQuestionResponse extends HintResponse{
    public QuestionResponse newQuestion;
}
