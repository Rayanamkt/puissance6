package com.example.power6game.model;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Game {
    private String gameId;
    private String player1Name;
    private String player2Name;
    private String password1;
    private String password2;
    private int player1TimeSeconds;
    private int player2TimeSeconds;
    private int player1RemainingTime;
    private int player2RemainingTime;
    private boolean player1Ready = false;
    private boolean player2Ready = false;
    private GameStatus status;
    private int currentPlayer; // 1 or 2
    private int[][] board; // 15x15 grid, 0=empty, 1=player1, 2=player2
    private LocalDateTime lastMoveTime;
    private int currentMoveCount = 1; // 0, 1, or 2 (for the 3 pieces per turn)
    private int[] pendingMoves;
    private String winner;

    public Game() {
        this.gameId = UUID.randomUUID().toString();
        this.password1 = UUID.randomUUID().toString().substring(0, 8);
        this.password2 = UUID.randomUUID().toString().substring(0, 8);
        this.board = new int[15][15];
        this.status = GameStatus.WAITING_FOR_PLAYER2;
        this.currentPlayer = 1;
        this.currentMoveCount = 0;
        this.pendingMoves = new int[3];
        this.winner = "";
    }

    // Getters and setters
    public String getId() { return gameId; }
    public void setId(String id) { this.gameId = id; }

    public String getPlayer1Name() { return player1Name; }
    public void setPlayer1Name(String player1Name) { this.player1Name = player1Name; }

    public String getPlayer2Name() { return player2Name; }
    public void setPlayer2Name(String player2Name) { this.player2Name = player2Name; }

    public String getPassword1() { return password1; }
    public void setPassword1(String password1) { this.password1 = password1; }

    public String getPassword2() { return password2; }
    public void setPassword2(String password2) { this.password2 = password2; }

    public int getPlayer1TimeSeconds() { return player1TimeSeconds; }
    public void setPlayer1TimeSeconds(int player1TimeSeconds) {
        this.player1TimeSeconds = player1TimeSeconds;
        this.player1RemainingTime = player1TimeSeconds;
    }

    public int getPlayer2TimeSeconds() { return player2TimeSeconds; }
    public void setPlayer2TimeSeconds(int player2TimeSeconds) {
        this.player2TimeSeconds = player2TimeSeconds;
        this.player2RemainingTime = player2TimeSeconds;
    }

    public int getPlayer1RemainingTime() { return player1RemainingTime; }
    public void setPlayer1RemainingTime(int player1RemainingTime) { this.player1RemainingTime = player1RemainingTime; }

    public int getPlayer2RemainingTime() { return player2RemainingTime; }
    public void setPlayer2RemainingTime(int player2RemainingTime) { this.player2RemainingTime = player2RemainingTime; }

    public boolean isPlayer1Ready() { return player1Ready; }
    public void setPlayer1Ready(boolean player1Ready) { this.player1Ready = player1Ready; }

    public boolean isPlayer2Ready() { return player2Ready; }
    public void setPlayer2Ready(boolean player2Ready) { this.player2Ready = player2Ready; }

    public GameStatus getStatus() { return status; }
    public void setStatus(GameStatus status) { this.status = status; }

    public int getCurrentPlayer() { return currentPlayer; }
    public void setCurrentPlayer(int currentPlayer) { this.currentPlayer = currentPlayer; }

    public int[][] getBoard() { return board; }
    public void setBoard(int[][] board) { this.board = board; }

    public LocalDateTime getLastMoveTime() { return lastMoveTime; }
    public void setLastMoveTime(LocalDateTime lastMoveTime) { this.lastMoveTime = lastMoveTime; }

    public int getCurrentMoveCount() { return currentMoveCount; }
    public void setCurrentMoveCount(int currentMoveCount) { this.currentMoveCount = currentMoveCount; }

    public int[] getPendingMoves() { return pendingMoves; }
    public void setPendingMoves(int[] pendingMoves) { this.pendingMoves = pendingMoves; }

    public String getCurrentPlayerPassword() {
        return currentPlayer == 1 ? password1 : password2;
    }
    public void setWinner(String winner) {
        this.winner = winner;
    }
    public String getWinner() {
        return winner;
    }
}

