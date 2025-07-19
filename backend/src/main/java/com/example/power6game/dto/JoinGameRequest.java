package com.example.power6game.dto;

import lombok.Data;

@Data
public class JoinGameRequest {
    private Long gameId;
    private String playerName;

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

}
