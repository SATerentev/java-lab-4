package com.simonheiss.back.usecase;

import com.simonheiss.back.DTO.*;
import com.simonheiss.back.DTO.Hints.*;
import com.simonheiss.back.entity.GameResult;
import com.simonheiss.back.entity.Hints;
import com.simonheiss.back.services.GameResultService;
import com.simonheiss.back.services.GameService;
import com.simonheiss.back.services.QuestionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
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

        int firstQuestionId = questionService.getNewQuestionId(1);
        UUID gameId = gameService.createGame(playerName, safeAmount, firstQuestionId);

        return gameId;
    }

    public GameResponse getGameInfo(UUID gameId){
        GameInfo data = gameService.getGameInfo(gameId);
        int payout = data.isActive ? data.currentScore : data.finalPayout;

        return new GameResponse(
                !data.isActive,
                gameId,
                data.currentQuestionId,
                data.playerName,
                data.currentScore,
                data.finalPayout,
                data.safeAmount,
                data.correctAnswers,
                data.usedHints,
                payout
        );
    }

    public QuestionResponse getQuestionInfo(UUID gameId){
        int questionId = gameService.getGameInfo(gameId).currentQuestionId;
        QuestionInfo data = questionService.getQuestionInfo(questionId);

        return new QuestionResponse(
                data.text,
                data.answers.get(0),
                data.answers.get(1),
                data.answers.get(2),
                data.answers.get(3)
        );
    }

    public void submitAnswer(UUID gameId, int answer){
        GameInfo data = gameService.getGameInfo(gameId);

        if (!data.isActive) throw new IllegalArgumentException("Игра уже завершена");
        if (answer > 4 || answer < 1) throw new IllegalArgumentException("Некорректный формат ответа");

        if (questionService.checkAnswer(data.currentQuestionId, answer)){
            if (gameService.checkMakeMistakeHint(gameId))
                gameService.deactivateMakeMistake(gameId);

            int newCurrentScore = PrizeTable[data.correctAnswers + 1];

            if (newCurrentScore == PrizeTable[PrizeTable.length - 1]){
                gameService.win(gameId, newCurrentScore);
                GameResult result = new GameResult(data.playerName, data.finalPayout, data.correctAnswers);
                gameResultService.save(result);
                return;
            }

            int nextGrade = data.correctAnswers + 2;
            int questionId = questionService.getNewQuestionId(nextGrade);
            gameService.moveToNextQuestion(gameId, questionId, newCurrentScore);
        } else {
            if (gameService.checkMakeMistakeHint(gameId)) {
                gameService.deactivateMakeMistake(gameId);
                return;
            }

            gameService.lose(gameId);
            GameResult gameResult = new GameResult(data.playerName, data.finalPayout, data.correctAnswers);
            gameResultService.save(gameResult);
        }
    }

    public HintResponse useHint(UUID gameId, Hints type){
        HintResponse response;

        switch (type){
            case Hints.ChangeQuestion:
                response = useChangeQuestion(gameId);
                break;
            case Hints.FiftyFifty:
                response = useFiftyFifty(gameId);
                break;
            case Hints.AskAudience:
                response = useAskAudience(gameId);
                break;
            case Hints.FriendCall:
                response = useFriendCall(gameId);
                break;
            case Hints.MakeMistake:
                response = useMakeMistake(gameId);
                break;
            default:
                throw new IllegalArgumentException("Такой тип подсказки не найден");
        }

        return response;
    }

    private HintResponse useChangeQuestion(UUID gameId){
        int correctAnswers = gameService.getGameInfo(gameId).correctAnswers;
        int nextQuestionId = questionService.getNewQuestionId(correctAnswers + 1);
        gameService.useChangeQuestion(gameId, nextQuestionId);

        QuestionInfo questionInfo = questionService.getQuestionInfo(nextQuestionId);
        QuestionResponse questionData = new QuestionResponse(
                questionInfo.text,
                questionInfo.answers.get(0),
                questionInfo.answers.get(1),
                questionInfo.answers.get(2),
                questionInfo.answers.get(3)
        );

        return new ChangeQuestionResponse(questionData);
    }

    private HintResponse useFiftyFifty(UUID gameId){
        gameService.useFiftyFifty(gameId);
        Random random = new Random();
        int questionId = gameService.getGameInfo(gameId).currentQuestionId;
        QuestionInfo questionInfo = questionService.getQuestionInfo(questionId);
        List<String> answers = questionInfo.answers;

        int correctAnswerIndex = questionInfo.correctAnswer - 1;
        answers.remove(correctAnswerIndex);

        String firstIncorrectAnswer = answers.get(random.nextInt(answers.size()));
        answers.remove(firstIncorrectAnswer);

        String secondIncorrectAnswer = answers.get(random.nextInt(answers.size()));

        return new FiftyFiftyResponse(firstIncorrectAnswer, secondIncorrectAnswer);
    }

    private HintResponse useAskAudience(UUID gameId){
        gameService.useAskAudience(gameId);

        String text = """
                Я всегда тебя предам
                И никогда не поддержу
                Дальше только меньше, брат
                Набери - я сброшу
                И твой годовой доход
                В рилсах поднимут за день
                Так что ни шагу вперед
                Только назад, парень
                
                Опускай руки, сдавайся
                Ты всегда последний, сколько бы ни старался
                На работе и в любви ты полный ноль и лузер
                Твоя жизнь дешевый фильм, где забухал продюсер
                
                Забывай о жизни, что ты обещал себе
                Принимай на свой счет все, кроме денег
                И все твои друзья тебя бросят в беде
                Себя нужно искать, а не делать
                Маленький ты тобой бы разочаровался
                Стремись стать человеком, с кем бы точно не встречался
                Пусть мечтами остаются все твои мечты
                И помни, что сдаются квартиры и ты
                
                Опускай руки, сдавайся
                Ты всегда последний, сколько бы ни старался
                На работе и в любви ты полный ноль и лузер
                Твоя жизнь дешевый фильм, где забухал продюсер
                
                Сдавайся.
                """;

        return new AskAudienceResponse(text);
    }

    private HintResponse useFriendCall(UUID gameId){
        gameService.useFriendCall(gameId);
        String text = "Понятия не имею. Сдавайся.";

        return new FriendCallResponse(text);
    }

    private HintResponse useMakeMistake(UUID gameId){
        gameService.useMakeMistake(gameId);

        return new HintResponse();
    }
}
