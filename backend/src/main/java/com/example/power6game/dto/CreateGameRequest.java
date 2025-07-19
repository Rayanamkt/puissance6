package com.example.power6game.dto;

import lombok.Data;

@Data
public class CreateGameRequest {
    private String player1Name;
    private String player2Name;

    private int player1TimeSeconds;
    private int player2TimeSeconds;
    private String playerName;

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

}

