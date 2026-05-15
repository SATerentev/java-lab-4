package com.simonheiss.back.DTO;

import com.simonheiss.back.entity.Hints;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class GameInfo {
    public boolean isActive;
    public String playerName;
    public int safeAmount;
    public int currentQuestionId;
    public int currentScore;
    public int correctAnswers;
    public List<Hints> usedHints;
    public boolean isMakeMistakeActive;
    public int finalPayout;
}
