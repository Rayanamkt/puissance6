package com.example.power6game.dto;

import lombok.Data;

@Data
public class GameStateResponse {
    private String gameId;
    private String player1Name;
    private String player2Name;
    private int currentPlayer;
    private int[][] board;
    private int player1RemainingTime;
    private int player2RemainingTime;
    private String status;
    private int currentMoveCount;
    private String winner;

}
