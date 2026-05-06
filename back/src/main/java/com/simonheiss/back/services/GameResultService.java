package com.simonheiss.back.services;

import com.simonheiss.back.entity.GameResult;
import com.simonheiss.back.infrastructure.GameResultRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameResultService {
    private final GameResultRepository repository;

    public GameResultService(GameResultRepository repository){
        this.repository = repository;
    }

    public List<GameResult> getTop10(){
        return repository.getTopResults();
    }

    public void save(GameResult result){
        repository.save(result);
    }
}

