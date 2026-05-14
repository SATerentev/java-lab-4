package com.simonheiss.back.usecase;

import com.simonheiss.back.DTO.GameResponse;
import com.simonheiss.back.DTO.QuestionResponse;
import com.simonheiss.back.entity.Game;
import com.simonheiss.back.entity.GameResult;
import com.simonheiss.back.entity.Question;
import com.simonheiss.back.services.GameResultService;
import com.simonheiss.back.services.GameService;
import com.simonheiss.back.services.QuestionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class GameUseCase {
    private final GameService gameService;
    private final GameResultService gameResultService;
    private final QuestionService questionService;

    private static final int[] PrizeTable = {
            0,
            500,
            1_000,
            2_000,
            3_000,
            5_000,
            10_000,
            15_000,
            25_000,
            50_000,
            100_000,
            200_000,
            400_000,
            800_000,
            1_500_000,
            3_000_000
    };


    public UUID createGame(String playerName, int safeAmount){
        if (playerName == null || playerName.isEmpty())
            throw new IllegalArgumentException("Имя игрока не может быть пустым");

        if (safeAmount < PrizeTable[0] || safeAmount > PrizeTable[PrizeTable.length - 1])
            throw new IllegalArgumentException("Несгораемая сумма вне диапазона.");

        Question firstQuestion = questionService.getNewQuestion(1);
        Game game = gameService.createGame(playerName, safeAmount, firstQuestion);

        return game.getId();
    }

    public GameResponse getGameInfo(UUID gameId){
        Game game = gameService.getGame(gameId);
        int payout = game.isActive() ? game.getCurrentScore() : game.getFinalPayout();
        return new GameResponse(
                !game.isActive(),
                game.getId(),
                game.getCurrentQuestionId(),
                game.getPlayerName(),
                game.getCurrentScore(),
                payout,
                game.getSafeAmount(),
                game.getCorrectAnswers()
        );
    }

    public QuestionResponse getQuestionInfo(int questionId){
        Question question = questionService.getQuestion(questionId);

        return new QuestionResponse(
                question.getText(),
                question.getAnswers().get(0),
                question.getAnswers().get(1),
                question.getAnswers().get(2),
                question.getAnswers().get(3)
        );
    }

    public void submitAnswer(UUID gameId, int answer){
        if (!gameService.getGame(gameId).isActive()) throw new IllegalArgumentException("Игра уже завершена");
        if (answer > 4 || answer < 1) throw new IllegalArgumentException("Некорректный формат ответа");

        Game game = gameService.getGame(gameId);

        if (questionService.checkAnswer(game.getCurrentQuestionId(), answer)){
            int newCurrentScore = PrizeTable[game.getCorrectAnswers() + 1];

            if (newCurrentScore == PrizeTable[PrizeTable.length - 1]){
                gameService.win(game, newCurrentScore);
                GameResult result = new GameResult(game.getPlayerName(), game.getFinalPayout(), game.getCorrectAnswers());
                gameResultService.save(result);
                return;
            }

            int nextGrade = game.getCorrectAnswers() + 2;
            int questionId = questionService.getNewQuestion(nextGrade).getId();
            gameService.moveToNextQuestion(game, questionId, newCurrentScore);
        } else {
            gameService.lose(game);
            GameResult gameResult = new GameResult(game.getPlayerName(), game.getFinalPayout(), game.getCorrectAnswers());
            gameResultService.save(gameResult);
        }
    }
}
