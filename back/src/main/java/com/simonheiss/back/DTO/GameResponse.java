package com.simonheiss.back.DTO;

import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class GameResponse {
    public boolean finished;
    public UUID gameId;
    public int questionId;
    public String playerName;
    public int score;
    public int payout;
    public int safeAmount;
    public int correctAnswers;
}
