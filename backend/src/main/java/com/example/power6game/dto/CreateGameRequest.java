package com.example.power6game.dto;

import lombok.Data;

@Data
public class CreateGameRequest {
    private String playerName;
    private int player1TimeSeconds;
    private int player2TimeSeconds;

}

