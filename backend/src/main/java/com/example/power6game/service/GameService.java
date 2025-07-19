package com.example.power6game.service;

import com.example.power6game.dto.GameStateResponse;
import com.example.power6game.dto.MakeMoveRequest;
import com.example.power6game.model.Game;
import com.example.power6game.model.GameStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GameService {

    private final Map<String, Game> games = new HashMap<>();

    public Game createGame(String player1Name, String player2Name, int player1Time, int player2Time) {
        Game game = new Game();
        game.setPlayer1Name(player1Name);
        game.setPlayer2Name(null); // pas encore défini
        game.setPlayer1TimeSeconds(player1Time);
        game.setPlayer2TimeSeconds(player2Time);
        game.setPlayer1Ready(false);
        game.setPlayer2Ready(false);
        games.put(game.getId(), game);
        return game;
    }

    public Game joinGame(String gameId, String player2Name) {
        Game game = games.get(gameId);

        if (game == null || game.getPlayer2Name() != null) {
            return null;
        }

        game.setPlayer2Name(player2Name);
        game.setPassword2(UUID.randomUUID().toString());

        if (game.getPlayer1Name() != null && game.getPlayer2Name() != null) {
            game.setStatus(GameStatus.IN_PROGRESS);
        }

        games.put(gameId, game);

        return game;
    }

    public Game getGame(String gameId) {
        return games.get(gameId);
    }

    public Game markPlayerReady(String gameId, String password) {
        Game game = getGame(gameId);

        if (game == null) {
            throw new IllegalArgumentException("Partie introuvable");
        }

        if (password.equals(game.getPassword1())) {
            game.setPlayer1Ready(true);
        } else if (password.equals(game.getPassword2())) {
            game.setPlayer2Ready(true);
        } else {
            throw new IllegalArgumentException("Mot de passe inconnu");
        }

        // Si les deux sont prêts, on passe en mode jeu
        if (game.isPlayer1Ready() && game.isPlayer2Ready()) {
            game.setStatus(GameStatus.IN_PROGRESS);
        }

        return game;
    }

    public List<Game> getAllGames() {
        return games.values().stream()
                .sorted(Comparator.comparing(Game::getLastMoveTime, Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());
    }

    public Collection<Game> listGames() {
        return games.values();
    }

    public Game makeMove(String gameId, String password, int column) {

        try {
            Game game = getGame(gameId);
            if (game == null) {
                throw new IllegalArgumentException("Partie introuvable");
            }


            int currentPlayer = game.getCurrentPlayer();
            System.out.println("🧠 currentMoveCount = " + game.getCurrentMoveCount());
            System.out.println("🎮 currentPlayer = " + currentPlayer);
            System.out.println("🔑 password reçu = " + password);
            System.out.println("🔐 password1 = " + game.getPassword1());
            System.out.println("🔐 password2 = " + game.getPassword2());

            // Vérifie que le mot de passe correspond au joueur en cours
            if ((currentPlayer == 1 && !password.equals(game.getPassword1())) ||
                    (currentPlayer == 2 && !password.equals(game.getPassword2()))) {
                throw new IllegalArgumentException("Mot de passe invalide pour ce joueur");
            }

            if (game.getStatus() != GameStatus.IN_PROGRESS) {
                throw new IllegalStateException("La partie n'est pas en cours");
            }

            if (column < 0 || column >= 15) {
                throw new IllegalArgumentException("Colonne invalide");
            }

            int row = -1;
            for (int i = 14; i >= 0; i--) {
                if (game.getBoard()[i][column] == 0) {
                    row = i;
                    break;
                }
            }

            if (row == -1) {
                throw new IllegalArgumentException("Colonne pleine");
            }


            game.getBoard()[row][column] = currentPlayer;
            game.getPendingMoves()[game.getCurrentMoveCount()] = column;
            game.setCurrentMoveCount(game.getCurrentMoveCount() + 1);

            if (checkVictory(game.getBoard(), row, column, currentPlayer)) {
                if (currentPlayer == 1) {
                    game.setStatus(GameStatus.FINISHED_PLAYER1_WIN);
                    game.setWinner(game.getPlayer1Name());  // ✅ ICI
                } else {
                    game.setStatus(GameStatus.FINISHED_PLAYER2_WIN);
                    game.setWinner(game.getPlayer2Name());  // ✅ ICI
                }
                return game;
            }

            if (isBoardFull(game.getBoard())) {
                game.setStatus(GameStatus.FINISHED_DRAW);
                game.setWinner("Match nul");
                return game;
            }

            if (game.getCurrentMoveCount() == 3) {
                game.setCurrentMoveCount(0);
                game.setCurrentPlayer(currentPlayer == 1 ? 2 : 1);
                game.setPendingMoves(new int[3]);
            }

            game.setLastMoveTime(java.time.LocalDateTime.now());

            return game;

        } catch (Exception e) {
            System.err.println("💥 Erreur backend lors de makeMove : " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    private boolean isBoardFull(int[][] board) {
        for (int[] row : board) {
            for (int cell : row) {
                if (cell == 0) return false;
            }
        }
        return true;
    }

    private boolean checkVictory(int[][] board, int row, int col, int player) {
        // Directions : horiz, vert, diag1, diag2
        int[][] directions = {
                {0, 1},  // →
                {1, 0},  // ↓
                {1, 1},  // ↘
                {1, -1}  // ↙
        };

        for (int[] dir : directions) {
            int count = 1;

            // Avance dans la direction
            count += countDirection(board, row, col, dir[0], dir[1], player);
            // Recule dans la direction opposée
            count += countDirection(board, row, col, -dir[0], -dir[1], player);

            if (count >= 6) return true;
        }

        return false;
    }

    private int countDirection(int[][] board, int row, int col, int dRow, int dCol, int player) {
        int count = 0;
        int i = row + dRow;
        int j = col + dCol;

        while (i >= 0 && i < board.length && j >= 0 && j < board[0].length && board[i][j] == player) {
            count++;
            i += dRow;
            j += dCol;
        }

        return count;
    }
}