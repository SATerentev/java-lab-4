package com.simonheiss.back.DTO;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CreateGameRequest {
    public String playerName;
    public int safeAmount;
}
