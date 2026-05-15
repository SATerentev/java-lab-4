package com.simonheiss.back.DTO.Hints;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FiftyFiftyResponse extends HintResponse{
    public String firstIncorrectAnswer;
    public String secondIncorrectAnswer;
}
