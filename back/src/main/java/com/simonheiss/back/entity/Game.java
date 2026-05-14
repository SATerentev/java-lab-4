package com.simonheiss.back.entity;

import lombok.Getter;

import java.util.UUID;

public class Game {
    @Getter
    private UUID id;
    @Getter
    private boolean isActive;
    @Getter
    private long finishedAt;
    @Getter
    private String playerName;
    @Getter
    private int safeAmount;
    @Getter
    private int currentQuestionId;
    @Getter
    private int currentScore;
    @Getter
    private int correctAnswers;
    @Getter
    private int usedHintsCount;
    @Getter
    private int finalPayout;

    public Game(String playerName, int safeAmount, int currentQuestionId){
        validatePlayerName(playerName);
        validateSafeAmount(safeAmount);
        validateCurrentQuestionId(currentQuestionId);

        id = UUID.randomUUID();
        isActive = true;
        this.playerName = playerName;
        this.safeAmount = safeAmount;
        this.currentQuestionId = currentQuestionId;
        currentScore = 0;
        correctAnswers = 0;
        usedHintsCount = 0;
        finalPayout = 0;
    }

    public void finish(){
        finishedAt = System.currentTimeMillis();
        isActive = false;
    }

    public void setCurrentQuestionId(int currentQuestionId){
        validateCurrentQuestionId(currentQuestionId);
        this.currentQuestionId = currentQuestionId;
    }

    public void setCurrentScore(int currentScore){
        validateCurrentScore(currentScore);
        this.currentScore = currentScore;
    }

    public void addCorrectAnswer(){
        correctAnswers++;
    }

    public void setFinalPayout(int payout){
        validateCurrentScore(payout);
        finalPayout = payout;
    }

    public void useHint(){
        usedHintsCount++;

        if (usedHintsCount > 4){
            throw new IllegalStateException("Нельзя использовать больше 4 подсказок.");
        }
    }

    private void validatePlayerName(String playerName){
        if (playerName == null || playerName.isEmpty())
            throw new IllegalArgumentException("Имя игрока не может быть пустым.");
    }

    private void validateCurrentQuestionId(int currentQuestionId){
        if (currentQuestionId <= 0)
            throw new IllegalArgumentException("ID вопроса не может быть меньше 1.");
    }

    private void validateSafeAmount(int safeAmount){
        if (safeAmount < 0)
            throw new IllegalArgumentException("Несгораемая сумма не может быть меньше 0.");
    }

    private void validateCurrentScore(int currentScore){
        if (currentScore < 0 || currentScore > 3_000_000)
            throw new IllegalArgumentException("Выигрыш не может быть меньше 0 или больше 3млн.");
    }
}
