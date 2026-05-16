package com.simonheiss.back.controllers;

import com.simonheiss.back.DTO.ResultsResponse;
import com.simonheiss.back.usecase.ResultUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/results")
public class ResultsController {
    private final ResultUseCase resultUseCase;

    @GetMapping("/top")
    public ResponseEntity<ResultsResponse> getTopResults(){
        var response = resultUseCase.getTopResults();

        return ResponseEntity.ok(response);
    }
}
