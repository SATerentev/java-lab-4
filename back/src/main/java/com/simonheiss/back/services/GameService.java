package com.simonheiss.back.services;

import com.simonheiss.back.DTO.GameInfo;
import com.simonheiss.back.entity.Game;
import com.simonheiss.back.entity.Hints;
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

    public UUID createGame(String player_name, int safeAmount, int firstQuestionId){
        Game game = new Game(player_name, safeAmount, firstQuestionId);
        games.put(game.getId(), game);

        return game.getId();
    }

    public GameInfo getGameInfo(UUID gameId){
        Game game = getGameAndValidate(gameId);

        return new GameInfo(
                game.isActive(),
                game.getPlayerName(),
                game.getSafeAmount(),
                game.getCurrentQuestionId(),
                game.getCurrentScore(),
                game.getCorrectAnswers(),
                game.getUsedHints(),
                game.isMakeMistakeActive(),
                game.getFinalPayout()
        );
    }

    public Game moveToNextQuestion(UUID gameId, int nextQuestionId, int newCurrentScore){
        Game game = getGameAndValidate(gameId);

        game.addCorrectAnswer();
        game.setCurrentScore(newCurrentScore);
        game.setCurrentQuestionId(nextQuestionId);

        return game;
    }

    public void win(UUID gameId, int prize){
        Game game = getGameAndValidate(gameId);

        game.addCorrectAnswer();
        game.setCurrentScore(prize);
        game.setFinalPayout(prize);
        game.finish();
    }

    public void lose(UUID gameId){
        Game game = getGameAndValidate(gameId);

        if (game.getCurrentScore() >= game.getSafeAmount())
            game.setFinalPayout(game.getSafeAmount());
        else
            game.setFinalPayout(0);

        game.finish();
    }

    public void useFiftyFifty(UUID gameId){
        Game game = getGameAndValidate(gameId);

        if (game.getUsedHints().contains(Hints.FiftyFifty))
            throw new IllegalStateException("Подсказка уже использована.");

        game.useHint(Hints.FiftyFifty);
    }

    public void useAskAudience(UUID gameId){
        Game game = getGameAndValidate(gameId);

        if (game.getUsedHints().contains(Hints.AskAudience))
            throw new IllegalStateException("Подсказка уже была использована.");

        game.useHint(Hints.AskAudience);
    }

    public void useFriendCall(UUID gameId){
        Game game = getGameAndValidate(gameId);

        if (game.getUsedHints().contains(Hints.FriendCall))
            throw new IllegalStateException("Подсказка уже была использована.");

        game.useHint(Hints.FriendCall);
    }

    public void useChangeQuestion(UUID gameId, int questionId){
        Game game = getGameAndValidate(gameId);
        if (game.getUsedHints().contains(Hints.ChangeQuestion))
            throw new IllegalStateException("Подсказка уже была использована.");

        game.setCurrentQuestionId(questionId);
        game.useHint(Hints.ChangeQuestion);
    }

    public void useMakeMistake(UUID gameId){
        Game game = getGameAndValidate(gameId);

        game.activateMakeMistake();
        game.useHint(Hints.MakeMistake);
    }

    public boolean checkMakeMistakeHint(UUID gameId){
        Game game = getGameAndValidate(gameId);

        return game.isMakeMistakeActive();
    }

    public void deactivateMakeMistake(UUID gameId){
        Game game = getGameAndValidate(gameId);

        game.deactivateMakeMistake();
    }

    @Scheduled(fixedRate = 60_000)
    public void cleanup(){
        long now = System.currentTimeMillis();

        games.entrySet().removeIf(entry -> !entry.getValue().isActive() && entry.getValue().getFinishedAt() + 600_000 < now);
    }

    private Game getGameAndValidate(UUID gameId){
        Game game = games.get(gameId);

        if (game == null) throw new IllegalArgumentException("Игра не найдена");

        return game;
    }
}
