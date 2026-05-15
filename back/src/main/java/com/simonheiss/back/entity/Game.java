package com.simonheiss.back.entity;

import lombok.Getter;

import java.util.*;

public class Game {
    private final int MAX_HINTS_COUNT = 4;

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
    private List<Hints> usedHints;
    @Getter
    private boolean isMakeMistakeActive;
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
        usedHints = new ArrayList<>();
        isMakeMistakeActive = false;
        finalPayout = 0;
    }

    public List<Hints> getUsedHints(){
        return List.copyOf(usedHints);
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

    public void useHint(Hints type){
        if (usedHints.contains(type))
            throw new IllegalArgumentException("Такая подсказка уже использована.");

        if (usedHints.size() >= MAX_HINTS_COUNT)
            throw new IllegalArgumentException("Нельзя использовать больше " + MAX_HINTS_COUNT + " подсказок за игру.");

        usedHints.add(type);
    }

    public void activateMakeMistake(){
        if (usedHints.contains(Hints.MakeMistake))
            throw new IllegalStateException("Такая подсказка уже использована.");

        isMakeMistakeActive = true;
    }

    public void deactivateMakeMistake(){
        if (!isMakeMistakeActive)
            throw new IllegalStateException("Ошибка при деактивации MakeMistakeHint: Подсказка не была активирована.");

        isMakeMistakeActive = false;
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
