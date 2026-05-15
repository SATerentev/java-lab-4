package com.simonheiss.back.DTO;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class QuestionInfo {
    public String text;
    public List<String> answers;
    public int correctAnswer;
    public int grade;
}
