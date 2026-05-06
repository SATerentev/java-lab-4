package com.simonheiss.back.DTO;

import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class AnswerRequest {
    public String gameId;
    public int answer;
}
