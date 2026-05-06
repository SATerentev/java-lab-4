package com.simonheiss.back.controllers;

import com.simonheiss.back.DTO.AnswerRequest;
import com.simonheiss.back.DTO.CreateGameRequest;
import com.simonheiss.back.DTO.GameResponse;
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
        System.out.println("/api/game/id  -   " + gameId);
        GameResponse response = gameUseCase.getGameInfo(UUID.fromString(gameId));

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
}
