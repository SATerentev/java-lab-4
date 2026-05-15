package com.simonheiss.back.controllers;

import com.simonheiss.back.DTO.*;
import com.simonheiss.back.DTO.Hints.HintRequest;
import com.simonheiss.back.DTO.Hints.HintResponse;
import com.simonheiss.back.entity.Hints;
import com.simonheiss.back.usecase.GameUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@AllArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/api/game")
public class GameController {
    private final GameUseCase gameUseCase;

    @GetMapping("/{gameId}")
    public ResponseEntity<GameResponse> getGame(@PathVariable String gameId){
        GameResponse response = gameUseCase.getGameInfo(UUID.fromString(gameId));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{gameId}/question/get")
    public ResponseEntity<QuestionResponse> getQuestion(@PathVariable String gameId){
        QuestionResponse response = gameUseCase.getQuestionInfo(UUID.fromString(gameId));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createGame(@RequestBody CreateGameRequest data){
        UUID gameId = gameUseCase.createGame(data.playerName, data.safeAmount);

        return ResponseEntity.ok(gameId.toString());
    }

    @PostMapping("/submitAnswer")
    public ResponseEntity<GameResponse> submitAnswer(@RequestBody AnswerRequest data){
        gameUseCase.submitAnswer(UUID.fromString(data.gameId), data.answer);

        GameResponse response = gameUseCase.getGameInfo(UUID.fromString(data.gameId));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{gameId}/useHint")
    public ResponseEntity<HintResponse> useHint(@PathVariable String gameId, @RequestBody HintRequest data){
        HintResponse response = gameUseCase.useHint(UUID.fromString(gameId), Hints.valueOf(data.HintName));

        return ResponseEntity.ok(response);
    }
}
