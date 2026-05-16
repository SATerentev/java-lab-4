package com.simonheiss.back.services;

import com.simonheiss.back.entity.GameResult;
import com.simonheiss.back.infrastructure.GameResultRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GameResultService {
    private final GameResultRepository repository;

    public GameResultService(GameResultRepository repository){
        this.repository = repository;
    }

    public List<String> getTop10(){
        List<GameResult> topResults = repository.getTopResults();
        List<String> result = new ArrayList<>();

        for (var i : topResults){
            result.addFirst(i.getPlayerName() + ":  " + i.getScore());
        }

        return result;
    }

    public void save(GameResult result){
        repository.save(result);
    }
}

