package com.example.power6game.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class CreateGameResponse {
    private String gameId;
    private String password;
    private int playerNumber;

    public CreateGameResponse(String gameId, String password, int playerNumber) {
        this.gameId = gameId;
        this.password = password;
        this.playerNumber = 1; // Toujours 1 pour le créateur
    }


}