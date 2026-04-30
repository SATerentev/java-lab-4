package com.simonheiss.back.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


@Entity
public class Question {
    @Id
    @GeneratedValue
    private Integer id;
    @Getter
    private String text;
    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;
    @Getter
    private int correctAnswer;
    @Getter
    private int grade;

    /// Для JPA
    public Question () { }

    public Question(String text, List<String> answers, int correctAnswer, int grade){
        validateText(text);
        validateAnswers(answers);
        validateCorrectAnswer(correctAnswer);
        validateGrade(grade);

        this.text = text;

        answer1 = answers.get(0);
        answer2 = answers.get(1);
        answer3 = answers.get(2);
        answer4 = answers.get(3);

        this.correctAnswer = correctAnswer;
        this.grade = grade;
    }

    public void setText(String text){
        validateText(text);
        this.text = text;
    }

    public void setAnswers(List<String> answers){
        validateAnswers(answers);
        answer1 = answers.get(0);
        answer2 = answers.get(1);
        answer3 = answers.get(2);
        answer4 = answers.get(3);
    }

    public List<String> getAnswers(){
        List<String> answers = new ArrayList<String>();
        answers.add(answer1);
        answers.add(answer2);
        answers.add(answer3);
        answers.add(answer4);

        return answers;
    }

    public void setCorrectAnswer(int answer){
        validateCorrectAnswer(answer);
        correctAnswer = answer;
    }

    public void setGrade(int grade){
        validateGrade(grade);
        this.grade = grade;
    }

    private void validateText(String text){
        if (text == null || text.isEmpty())
            throw new IllegalArgumentException("Текст вопроса не может быть пустым.");
    }

    private void validateAnswers(List<String> answers){
        if (answers == null || answers.size() != 4)
            throw new IllegalArgumentException("Ответов должно быть ровно 4.");

        for (var answer : answers){
            if (answer == null || answer.isEmpty())
                throw new IllegalArgumentException("Ответ не может быть пустым.");
        }
    }

    private void validateCorrectAnswer(int correctAnswer){
        if (correctAnswer > 4 || correctAnswer < 1)
            throw new IllegalArgumentException("Правильный ответ строго от 1 до 4");
    }

    private void validateGrade(int grade){
        if (grade < 1 || grade > 15)
            throw new IllegalArgumentException("Номер вопроса строго от 1 до 15.");
    }
}
