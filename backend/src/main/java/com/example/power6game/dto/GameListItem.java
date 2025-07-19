package com.example.power6game.dto;

import com.example.power6game.model.GameStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GameListItem {
    private String gameId;
    private String player1Name;
    private String player2Name;
    private int player1TimeSeconds;
    private int player2TimeSeconds;
    private GameStatus status;
    private String winner;
}
