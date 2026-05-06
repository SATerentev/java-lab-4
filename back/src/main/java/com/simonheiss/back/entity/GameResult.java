package com.simonheiss.back.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
public class GameResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Getter
    private String playerName;
    @Getter
    private int score;
    @Getter
    private int correctAnswers;
    @Getter
    private LocalDateTime createdAt;

    public GameResult() { } // JPA

    public GameResult(String playerName, int score, int correctAnswers){
        validatePlayerName(playerName);
        validateScore(score);
        validateCorrectAnswers(correctAnswers);

        this.playerName = playerName;
        this.score = score;
        this.correctAnswers = correctAnswers;
        createdAt = LocalDateTime.now();
    }

    private void validatePlayerName(String name){
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("Имя игрока не может быть пустым.");
    }

    private void validateScore(int score){
        if (score < 0 || score > 3_000_000) // Тут бы вообще сделать что-то типа class GameProperties, откуда будет браться значение, но как-то лень
            throw new IllegalArgumentException("Выигрыш не может быть меньше 0 или больше 3млн.");
    }

    private void validateCorrectAnswers(int correctAnswers){
        if (correctAnswers < 0 || correctAnswers > 15) // Тут такая же ситуация, просто лень
            throw new IllegalArgumentException("Количество правильных ответов не может быть меньше 0 или больше 15.");
    }
}
