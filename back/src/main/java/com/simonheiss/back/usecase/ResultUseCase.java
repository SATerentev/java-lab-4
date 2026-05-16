package com.simonheiss.back.usecase;

import com.simonheiss.back.DTO.ResultsResponse;
import com.simonheiss.back.services.GameResultService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ResultUseCase {
    private final GameResultService gameResultService;

    public ResultsResponse getTopResults(){
        var data = gameResultService.getTop10();
        var results = new ArrayList<String>();

        for (var i : data){
            results.addFirst(i);
        }

        return new ResultsResponse(results);
    }
}
