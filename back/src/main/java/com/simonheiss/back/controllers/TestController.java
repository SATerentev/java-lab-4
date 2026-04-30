package com.simonheiss.back.controllers;

import com.simonheiss.back.entity.Question;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/api/test")
public class TestController {
    private final Random rand = new Random();

    @GetMapping("/getNum")
    public int getNum(){
        return rand.nextInt(1000);
    }
}
