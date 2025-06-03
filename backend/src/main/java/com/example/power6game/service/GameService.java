package com.example.power6game.service;

import com.example.power6game.model.Game;
import com.example.power6game.model.GameStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameService {
    private final Map<String, Game> games = new ConcurrentHashMap<>();

    public Game createGame(String playerName, int player1Time, int player2Time) {
        Game game = new Game();
        game.setPlayer1Name(playerName);
        game.setPlayer1TimeSeconds(player1Time);
        game.setPlayer2TimeSeconds(player2Time);
        games.put(game.getId(), game);
        return game;
    }

    public List<Game> getWaitingGames() {
        return games.values().stream()
                .filter(game -> game.getStatus() == GameStatus.WAITING_FOR_PLAYER2)
                .toList();
    }

    public Game joinGame(String gameId, String playerName) {
        Game game = games.get(gameId);
        if (game == null || game.getStatus() != GameStatus.WAITING_FOR_PLAYER2) {
            return null;
        }

        game.setPlayer2Name(playerName);
        game.setStatus(GameStatus.IN_PROGRESS);
        game.setLastMoveTime(LocalDateTime.now());
        return game;
    }

    public Game getGame(String gameId) {
        return games.get(gameId);
    }

    public Game makeMove(String gameId, String password, int column) {
        Game game = games.get(gameId);
        if (game == null || game.getStatus() != GameStatus.IN_PROGRESS) {
            return null;
        }

        // Verify password
        boolean isPlayer1 = password.equals(game.getPassword1());
        boolean isPlayer2 = password.equals(game.getPassword2());

        if (!isPlayer1 && !isPlayer2) {
            return null;
        }

        // Check if it's the correct player's turn
        int playerNumber = isPlayer1 ? 1 : 2;
        if (playerNumber != game.getCurrentPlayer()) {
            return null;
        }

        // Update remaining time
        updatePlayerTime(game);

        // Check if column is valid and not full
        if (column < 0 || column >= 15 || isColumnFull(game.getBoard(), column)) {
            return null;
        }

        // Place the piece
        int row = getNextAvailableRow(game.getBoard(), column);
        game.getBoard()[row][column] = playerNumber;

        // Store pending move
        game.getPendingMoves()[game.getCurrentMoveCount()] = column;
        game.setCurrentMoveCount(game.getCurrentMoveCount() + 1);

        // Check if this completes the turn (3 pieces)
        if (game.getCurrentMoveCount() >= 3) {
            // Check for win
            if (checkWin(game.getBoard(), playerNumber)) {
                game.setStatus(playerNumber == 1 ? GameStatus.FINISHED_PLAYER1_WIN : GameStatus.FINISHED_PLAYER2_WIN);
            } else if (isBoardFull(game.getBoard())) {
                game.setStatus(GameStatus.FINISHED_DRAW);
            } else {
                // Switch player
                game.setCurrentPlayer(game.getCurrentPlayer() == 1 ? 2 : 1);
                game.setCurrentMoveCount(0);
                game.setLastMoveTime(LocalDateTime.now());
            }
        }

        return game;
    }

    private void updatePlayerTime(Game game) {
        if (game.getLastMoveTime() == null) return;

        long secondsElapsed = ChronoUnit.SECONDS.between(game.getLastMoveTime(), LocalDateTime.now());

        if (game.getCurrentPlayer() == 1) {
            game.setPlayer1RemainingTime(Math.max(0, game.getPlayer1RemainingTime() - (int) secondsElapsed));
            if (game.getPlayer1RemainingTime() <= 0) {
                game.setStatus(GameStatus.FINISHED_TIME_OUT);
            }
        } else {
            game.setPlayer2RemainingTime(Math.max(0, game.getPlayer2RemainingTime() - (int) secondsElapsed));
            if (game.getPlayer2RemainingTime() <= 0) {
                game.setStatus(GameStatus.FINISHED_TIME_OUT);
            }
        }
    }

    private boolean isColumnFull(int[][] board, int column) {
        return board[0][column] != 0;
    }

    private int getNextAvailableRow(int[][] board, int column) {
        for (int row = 14; row >= 0; row--) {
            if (board[row][column] == 0) {
                return row;
            }
        }
        return -1;
    }

    private boolean checkWin(int[][] board, int player) {
        // Check horizontal, vertical, and diagonal for 6 in a row
        return checkHorizontal(board, player) ||
                checkVertical(board, player) ||
                checkDiagonal(board, player);
    }

    private boolean checkHorizontal(int[][] board, int player) {
        for (int row = 0; row < 15; row++) {
            int count = 0;
            for (int col = 0; col < 15; col++) {
                if (board[row][col] == player) {
                    count++;
                    if (count >= 6) return true;
                } else {
                    count = 0;
                }
            }
        }
        return false;
    }

    private boolean checkVertical(int[][] board, int player) {
        for (int col = 0; col < 15; col++) {
            int count = 0;
            for (int row = 0; row < 15; row++) {
                if (board[row][col] == player) {
                    count++;
                    if (count >= 6) return true;
                } else {
                    count = 0;
                }
            }
        }
        return false;
    }

    private boolean checkDiagonal(int[][] board, int player) {
        // Check diagonal (top-left to bottom-right)
        for (int row = 0; row <= 9; row++) {
            for (int col = 0; col <= 9; col++) {
                int count = 0;
                for (int i = 0; i < 6 && row + i < 15 && col + i < 15; i++) {
                    if (board[row + i][col + i] == player) {
                        count++;
                    } else {
                        break;
                    }
                }
                if (count >= 6) return true;
            }
        }

        // Check diagonal (top-right to bottom-left)
        for (int row = 0; row <= 9; row++) {
            for (int col = 5; col < 15; col++) {
                int count = 0;
                for (int i = 0; i < 6 && row + i < 15 && col - i >= 0; i++) {
                    if (board[row + i][col - i] == player) {
                        count++;
                    } else {
                        break;
                    }
                }
                if (count >= 6) return true;
            }
        }

        return false;
    }

    private boolean isBoardFull(int[][] board) {
        for (int col = 0; col < 15; col++) {
            if (board[0][col] == 0) {
                return false;
            }
        }
        return true;
    }
}