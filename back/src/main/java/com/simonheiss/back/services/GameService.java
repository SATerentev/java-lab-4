package com.simonheiss.back.services;

import com.simonheiss.back.entity.Game;
import com.simonheiss.back.entity.Question;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class GameService {
    private final Map<UUID, Game> games; // Вообще это бы в redis запихнуть, но докеры для лабы это уже перебор

    public GameService(){
        games = new HashMap<>();
    }

    public Game createGame(String player_name, int safeAmount, Question firstQuestion){
        Game game;

        game = new Game(player_name, safeAmount, firstQuestion.getId());
        games.put(game.getId(), game);

        return game;
    }

    public Game getGame(UUID gameId){
        Game game = games.get(gameId);

        if (game == null) throw new IllegalArgumentException("Игра с таким id не найдена");

        return game;
    }

    public Game moveToNextQuestion(Game game, int nextQuestionId, int newCurrentScore){
        game.addCorrectAnswer();
        game.setCurrentScore(newCurrentScore);
        game.setCurrentQuestionId(nextQuestionId);

        return game;
    }

    public void win(Game game, int prize){
        game.addCorrectAnswer();
        game.setCurrentScore(prize);
        game.setFinalPayout(prize);
        game.finish();
    }

    public void lose(Game game){
        if (game.getCurrentScore() >= game.getSafeAmount())
            game.setFinalPayout(game.getSafeAmount());
        else
            game.setFinalPayout(0);

        game.finish();
    }

    @Scheduled(fixedRate = 60_000)
    public void cleanup(){
        long now = System.currentTimeMillis();

        games.entrySet().removeIf(entry -> !entry.getValue().isActive() && entry.getValue().getFinishedAt() + 600_000 < now);
    }
}
